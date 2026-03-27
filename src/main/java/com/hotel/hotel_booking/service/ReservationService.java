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
/**
 * @Transactional: Đảm bảo tính toàn vẹn dữ liệu. 
 * Nếu có lỗi xảy ra trong quá trình tính toán hoặc lưu DB, 
 * mọi thay đổi trước đó trong cùng một hàm sẽ được rollback (hủy bỏ).
 */
@Transactional
public class ReservationService {

    @Autowired private ReservationRepository reservationRepo;
    @Autowired private ExtraServiceRepository extraServiceRepo;
    @Autowired private ReservationAddOnRepository addOnRepo;
    @Autowired private UserRepository userRepo;

    /**
     * Khởi tạo giỏ hàng (Trạng thái tạm thời)
     */
    public Reservation createCart(Reservation res) {
        validateDates(res.getCheckInDate(), res.getCheckOutDate());
        res.setStatus("Cart");
        
        // Tính tổng tiền tạm tính ngay khi tạo giỏ hàng để UI hiển thị số liệu ban đầu
        calculateAndSetTotal(res); 
        return reservationRepo.save(res);
    }

    /**
     * Thêm dịch vụ đi kèm và CẬP NHẬT LẠI TỔNG TIỀN
     */
    public void addExtraService(int resId, int serviceId, int quantity) {
        // .orElseThrow(): Cú pháp Java 8 giúp ném lỗi ngay nếu không tìm thấy ID
        Reservation res = reservationRepo.findById(resId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        if (!"Cart".equals(res.getStatus())) throw new RuntimeException("Đơn hàng đã chốt!");

        ExtraService service = extraServiceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại!"));
        
        // Lưu thông tin dịch vụ vào bảng trung gian ReservationAddOn
        ReservationAddOn addOn = new ReservationAddOn();
        addOn.setReservation(res);
        addOn.setExtraService(service);
        addOn.setQuantity(quantity);
        addOn.setPriceAtBooking(service.getPrice()); // Snapshot giá tại thời điểm khách đặt

        addOnRepo.save(addOn);
        //Thêm trực tiếp vào list trong object res để calculateAndSetTotal thấy nó
        res.getReservationAddOns().add(addOn);
        
        calculateAndSetTotal(res);// Tính lại giá tiền
        reservationRepo.save(res);
    }

    /**
     * Logic trung tâm: Tính toán tổng chi phí dựa trên thời gian ở và dịch vụ
     */
    private void calculateAndSetTotal(Reservation res) {
        long nights = ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate());
        if (nights < 1) nights = 1;

        double roomRate = (res.getRoomType() != null && res.getRoomType().getBasePrice() != null) 
                ? res.getRoomType().getBasePrice().doubleValue() : 0.0;
                
        double packageRate = (res.getServicePackage() != null && res.getServicePackage().getPackagePrice() != null) 
                ? res.getServicePackage().getPackagePrice().doubleValue() : 0.0;
        
        double addOnTotal = 0.0;
        if (res.getReservationAddOns() != null) {
            addOnTotal = res.getReservationAddOns().stream()
                    .filter(a -> a.getPriceAtBooking() != null) // Lọc bỏ nếu giá bị null
                    .mapToDouble(a -> a.getPriceAtBooking().doubleValue() * a.getQuantity())
                    .sum();
        }

        double total = ((roomRate + packageRate) * res.getRoomQuantity() * nights) + addOnTotal;
        res.setTotalAmount(total);
    }

    /**
     * Chuyển trạng thái sang Confirmed và cấp mã Voucher
     */
    public Reservation confirmBooking(int resId) {
        Reservation res = reservationRepo.findById(resId).orElseThrow();
        validateDates(res.getCheckInDate(), res.getCheckOutDate());

        // Đảm bảo tiền bạc được tính toán chuẩn xác lần cuối
        calculateAndSetTotal(res);
        
        res.setStatus("Confirmed");
        
        // UUID: Tạo một chuỗi định danh ngẫu nhiên không trùng lặp để làm mã Voucher
        res.setConfirmationVoucher("VOU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        return reservationRepo.save(res);
    }
    
    /**
     * Lấy lịch sử: Sử dụng Query Method của Spring Data JPA
     * (Tìm theo UserId, Trạng thái khác 'Cart', Sắp xếp ID giảm dần)
     */
    public List<Reservation> getHistoryByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) return List.of();        
        return reservationRepo.findByUser_UserIdAndStatusNotOrderByReservationIdDesc(user.getUserId(), "Cart");
    }

    /**
     * Kiểm tra tính hợp lệ của ngày đặt phòng
     */
    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new RuntimeException("Thiếu ngày tháng!");
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime limitTime = LocalTime.of(12, 0); // Quy định 12:00 trưa để check-in trong ngày

        if (in.equals(today) && now.isAfter(limitTime)) {
            throw new RuntimeException("Đã quá 12:00 trưa, vui lòng đặt phòng từ ngày mai!");
        }

        if (in.isBefore(today)) {
            throw new RuntimeException("Ngày nhận phòng không thể ở quá khứ!");
        }

        // !out.isAfter(in): Ngày trả phải ít nhất là sau ngày nhận 1 ngày
        if (!out.isAfter(in)) {
            throw new RuntimeException("Ngày trả phòng phải sau ngày nhận ít nhất 1 đêm!");
        }
    }

    public Reservation getById(Integer id) {
        return reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + id));
    }
    
    public List<Reservation> searchReservations(String keyword, String status) {
        List<Reservation> all = reservationRepo.findAll();
        return all.stream()
            .filter(r -> keyword == null || keyword.isBlank() || 
                    (r.getConfirmationVoucher() != null && r.getConfirmationVoucher().contains(keyword)) ||
                    (r.getUser() != null && r.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase())))
            .filter(r -> status == null || status.isBlank() || r.getStatus().equalsIgnoreCase(status))
            .toList();
    }
}