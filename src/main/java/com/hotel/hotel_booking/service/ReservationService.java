package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.*;
import com.hotel.hotel_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ReservationService {

    @Autowired private ReservationRepository reservationRepo;
    @Autowired private ExtraServiceRepository extraServiceRepo;
    @Autowired private ReservationAddOnRepository addOnRepo;

    // Bước A: Khởi tạo giỏ hàng 
    public Reservation createCart(Reservation reservation) {
        reservation.setStatus("Cart"); 
        return reservationRepo.save(reservation);
    }

    // Bước B: Thêm dịch vụ đi kèm 
    public void addExtraService(int resId, int serviceId, int quantity) {
        Reservation res = reservationRepo.findById(resId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng: " + resId));
        
        ExtraService service = extraServiceRepo.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        ReservationAddOn addOn = new ReservationAddOn();
        addOn.setReservation(res);
        addOn.setExtraService(service); // Khớp với gợi ý của IDE
        addOn.setQuantity(quantity);
        addOn.setPriceAtBooking(service.getPrice()); // Chốt giá tại thời điểm đặt

        addOnRepo.save(addOn);
    }

    // Bước C: Tính tổng tiền và xác nhận
    public Reservation confirmBooking(int resId) {
        Reservation res = reservationRepo.findById(resId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Lấy giá từ các thực thể liên quan 
        double roomBasePrice = res.getRoomType().getBasePrice().doubleValue();
        double packagePrice = res.getServicePackage().getPackagePrice().doubleValue();
        
        // Tính tổng tiền add-ons từ danh sách JPA Mapping [
        double addOnTotal = 0;
        if (res.getReservationAddOns() != null) {
            addOnTotal = res.getReservationAddOns().stream()
                    .mapToDouble(a -> a.getPriceAtBooking().doubleValue() * a.getQuantity())
                    .sum();
        }

        double finalTotal = ((roomBasePrice + packagePrice) * res.getRoomQuantity()) + addOnTotal;
        
        res.setTotalAmount(finalTotal);
        res.setStatus("Confirmed");
        res.setConfirmationVoucher("VOU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); 
        return reservationRepo.save(res);
    }
}