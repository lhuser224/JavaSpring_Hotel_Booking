package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.*;
import com.hotel.hotel_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional// đảm bảo dùng transaction trong sql
public class ReservationService {

    @Autowired private ReservationRepository reservationRepo;
    @Autowired private ExtraServiceRepository extraServiceRepo;
    @Autowired private ReservationAddOnRepository addOnRepo;
    @Autowired private UserRepository userRepo;

    /**
     * Bước A: Khởi tạo giỏ hàng + Kiểm tra ràng buộc ngày tháng
     */
    public Reservation createCart(Reservation res) {
        validateDates(res.getCheckInDate(), res.getCheckOutDate());
        res.setStatus("Cart");
        return reservationRepo.save(res);
    }

    /**
     * Bước B: Thêm dịch vụ đi kèm (Chỉ cho phép khi đơn là 'Cart')
     */
    public void addExtraService(int resId, int serviceId, int quantity) {
        Reservation res = reservationRepo.findById(resId).orElseThrow();
        if (!"Cart".equals(res.getStatus())) throw new RuntimeException("Đơn hàng đã chốt!");

        ExtraService service = extraServiceRepo.findById(serviceId).orElseThrow();
        
        ReservationAddOn addOn = new ReservationAddOn();
        addOn.setReservation(res);
        addOn.setExtraService(service);
        addOn.setQuantity(quantity);
        addOn.setPriceAtBooking(service.getPrice()); // Chốt giá hiện tại

        addOnRepo.save(addOn);
    }

    /**
     * Bước C: Tính tiền theo Số đêm & Xác nhận
     * Công thức: ((Phòng + Gói) * Số lượng * Số đêm) + Dịch vụ thêm
     */
    public Reservation confirmBooking(int resId) {
        Reservation res = reservationRepo.findById(resId).orElseThrow();
        validateDates(res.getCheckInDate(), res.getCheckOutDate());

        // Core Logic: Tính số đêm lưu trú
        long nights = ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate());
        if (nights < 1) nights = 1;

        double roomRate = res.getRoomType().getBasePrice().doubleValue();
        double packageRate = res.getServicePackage().getPackagePrice().doubleValue();
        
        double addOnTotal = res.getReservationAddOns().stream()
                .mapToDouble(a -> a.getPriceAtBooking().doubleValue() * a.getQuantity()).sum();

        // Tổng tiền bao gồm thời gian ở
        double total = ((roomRate + packageRate) * res.getRoomQuantity() * nights) + addOnTotal;

        res.setTotalAmount(total);
        res.setStatus("Confirmed");
        res.setConfirmationVoucher("VOU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return reservationRepo.save(res);
    }
    
    public List<Reservation> getHistoryByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) return List.of();        
        // Lấy tất cả trừ trạng thái "Cart"
        return reservationRepo.findByUser_UserIdAndStatusNotOrderByReservationIdDesc(user.getUserId(), "Cart");
    }

    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new RuntimeException("Thiếu ngày tháng!");
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime limitTime = LocalTime.of(12, 0); // Quy định 12:00 trưa

        // 1. Chặn nếu ngày nhận phòng là hôm nay nhưng đã quá 12:00 trưa
        if (in.equals(today) && now.isAfter(limitTime)) {
            throw new RuntimeException("Đã quá 12:00 trưa, vui lòng đặt phòng từ ngày mai!");
        }

        // 2. Chặn nếu ngày nhận trong quá khứ
        if (in.isBefore(today)) {
            throw new RuntimeException("Ngày nhận phòng không hợp lệ!");
        }

        // 3. Chặn ngày trả phòng
        if (!out.isAfter(in)) {
            throw new RuntimeException("Ngày trả phòng phải sau ngày nhận ít nhất 1 ngày!");
        }
    }
    public Reservation getById(Integer id) {
        return reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
    }
}