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
@Transactional
public class ReservationService {

    @Autowired private ReservationRepository reservationRepo;
    @Autowired private ExtraServiceRepository extraServiceRepo;
    @Autowired private ReservationAddOnRepository addOnRepo;
    @Autowired private UserRepository userRepo;

    public Reservation createCart(Reservation res) {
        validateDates(res.getCheckInDate(), res.getCheckOutDate());
        res.setStatus("Cart");
        calculateAndSetTotal(res); 
        return reservationRepo.save(res);
    }

    public void addExtraService(int resId, int serviceId, int quantity) {
        Reservation res = reservationRepo.findById(resId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        
        if (!"Cart".equals(res.getStatus())) throw new RuntimeException("Đơn hàng đã chốt!");

        ExtraService service = extraServiceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại!"));
        
        ReservationAddOn addOn = new ReservationAddOn();
        addOn.setReservation(res);
        addOn.setExtraService(service);
        addOn.setQuantity(quantity);
        addOn.setPriceAtBooking(service.getPrice());

        addOnRepo.save(addOn);
        res.getReservationAddOns().add(addOn);
        
        calculateAndSetTotal(res);
        reservationRepo.save(res);
    }

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
                    .filter(a -> a.getPriceAtBooking() != null)
                    .mapToDouble(a -> a.getPriceAtBooking().doubleValue() * a.getQuantity())
                    .sum();
        }

        double total = ((roomRate + packageRate) * res.getRoomQuantity() * nights) + addOnTotal;
        res.setTotalAmount(total);
    }

    public Reservation confirmBooking(int resId) {
        Reservation res = reservationRepo.findById(resId).orElseThrow();
        validateDates(res.getCheckInDate(), res.getCheckOutDate());

        // Sử dụng hàm query từ repository để tìm danh sách phòng vật lý trống
        List<Room> availableRooms = reservationRepo.findAvailablePhysicalRooms(
                res.getRoomType().getRoomTypeId(), 
                res.getCheckInDate(), 
                res.getCheckOutDate()
        );

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Hết phòng thực tế trống cho loại này trong thời gian đã chọn!");
        }

        // Tự động gán phòng đầu tiên tìm được
        res.setRoom(availableRooms.get(0));

        calculateAndSetTotal(res);
        res.setStatus("Confirmed");
        res.setConfirmationVoucher("VOU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        return reservationRepo.save(res);
    }
    
    public List<Reservation> getHistoryByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) return List.of();        
        return reservationRepo.findByUser_UserIdAndStatusNotOrderByReservationIdDesc(user.getUserId(), "Cart");
    }

    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new RuntimeException("Thiếu ngày tháng!");
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime limitTime = LocalTime.of(12, 0);

        if (in.equals(today) && now.isAfter(limitTime)) {
            throw new RuntimeException("Đã quá 12:00 trưa, vui lòng đặt phòng từ ngày mai!");
        }
        if (in.isBefore(today)) {
            throw new RuntimeException("Ngày nhận phòng không thể ở quá khứ!");
        }
        if (!out.isAfter(in)) {
            throw new RuntimeException("Ngày trả phòng phải sau ngày nhận ít nhất 1 đêm!");
        }
    }

    public Reservation getById(Integer id) {
        return reservationRepo.findById(id).orElseThrow();
    }
    
    public List<Reservation> searchReservations(String keyword, String status) {
        return reservationRepo.findAll().stream()
            .filter(r -> keyword == null || keyword.isBlank() || 
                    (r.getConfirmationVoucher() != null && r.getConfirmationVoucher().contains(keyword)) ||
                    (r.getUser() != null && r.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase())))
            .filter(r -> status == null || status.isBlank() || r.getStatus().equalsIgnoreCase(status))
            .toList();
    }
}