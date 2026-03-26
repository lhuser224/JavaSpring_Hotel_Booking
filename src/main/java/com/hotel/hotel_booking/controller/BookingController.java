package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.Reservation;
import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.entity.RoomType;
import com.hotel.hotel_booking.entity.ServicePackage;
import com.hotel.hotel_booking.service.ReservationService;
import com.hotel.hotel_booking.service.RoomService;
import com.hotel.hotel_booking.service.UserService;
import com.hotel.hotel_booking.service.HotelExtraService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired private ReservationService reservationService;
    @Autowired private RoomService roomService;
    @Autowired private HotelExtraService hotelExtraService;
    @Autowired private UserService userService;

    // 1. Hiển thị form chọn phòng ban đầu
    @GetMapping("/select")
    public String selectRoomType(@RequestParam("typeId") Integer typeId, Model model) {
        // 1. Lấy thông tin loại phòng
        RoomType selectedType = roomService.getRoomTypeById(typeId);
        
        // 2. Khởi tạo đối tượng Reservation (Form Object)
        Reservation reservation = new Reservation();
        reservation.setRoomType(selectedType);
        reservation.setRoomQuantity(1);
        reservation.setGuestCount(1); 

        // 3. Lấy danh sách gói dịch vụ từ Service
        List<ServicePackage> packages = hotelExtraService.getAllPackages();
        
        // Tìm gói Standard làm mặc định nếu có
        ServicePackage defaultPkg = packages.stream()
                .filter(p -> "Standard".equalsIgnoreCase(p.getPackageName()))
                .findFirst()
                .orElse(!packages.isEmpty() ? packages.get(0) : null);
        reservation.setServicePackage(defaultPkg);

        // 4. Đưa dữ liệu sang View
        model.addAttribute("reservation", reservation);
        model.addAttribute("packages", packages);
        model.addAttribute("selectedRoomType", selectedType);

        return "booking/create"; 
    }

    // 2. Lưu thông tin cơ bản vào giỏ hàng (Trạng thái "Cart")
    @PostMapping("/save-cart")
    public String saveCart(@ModelAttribute("reservation") Reservation reservation, Principal principal) {
        if (principal == null) return "redirect:/login";
        if (reservation.getRoomType() == null || reservation.getRoomType().getRoomTypeId() == null) {
            return "redirect:/";
        }
        User currentUser = userService.findByUsername(principal.getName());
        reservation.setUser(currentUser);        
        Reservation saved = reservationService.createCart(reservation);        
        return "redirect:/bookings/details/" + saved.getReservationId();
    }

    // 3. Trang chi tiết đơn hàng (Xem lại & Thêm dịch vụ Add-on)
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Integer id, Model model) {
        Reservation res = reservationService.getById(id);
        
        // Tính số đêm (ChronoUnit.DAYS)
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            res.getCheckInDate(), 
            res.getCheckOutDate()
        );
        if (nights <= 0) nights = 1; 

        model.addAttribute("reservation", res);
        model.addAttribute("id", id);
        model.addAttribute("nights", nights); 
        model.addAttribute("extraServices", hotelExtraService.getAllExtraServices());
        
        return "booking/details";
    }

    // 4. Xác nhận đặt phòng -> Đổi trạng thái sang "Confirmed" và cấp Voucher
    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Integer id, Model model) {
        Reservation res = reservationService.confirmBooking(id);
        model.addAttribute("reservation", res);
        return "booking/voucher"; // Trả về trang hiển thị mã Voucher
    }
    
    @GetMapping("/history")
    public String showHistory(Principal principal, Model model) {
        String username = principal.getName();
        List<Reservation> history = reservationService.getHistoryByUsername(username);
        
        model.addAttribute("historyList", history);
        return "booking/history"; 
    }
    
}