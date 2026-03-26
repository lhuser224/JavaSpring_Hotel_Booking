package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.Reservation;
import com.hotel.hotel_booking.entity.User;
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
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes()); 
        model.addAttribute("packages", hotelExtraService.getAllPackages()); 
        model.addAttribute("extraServices", hotelExtraService.getAllExtraServices()); 
        model.addAttribute("reservation", new Reservation());
        return "booking/create"; 
    }

    // 2. Lưu thông tin cơ bản vào giỏ hàng (Trạng thái "Cart")
    @PostMapping("/save-cart")
    public String saveCart(@ModelAttribute("reservation") Reservation reservation, Principal principal) {
        // 1. Lấy thông tin User thông qua Service
        String username = principal.getName();
        User currentUser = userService.findByUsername(username); 
        
        // 2. Gán User vào đơn hàng
        reservation.setUser(currentUser);
        
        // 3. Tạo giỏ hàng
        Reservation saved = reservationService.createCart(reservation);
        return "redirect:/bookings/details/" + saved.getReservationId();
    }

    // 3. Trang chi tiết đơn hàng (Xem lại & Thêm dịch vụ Add-on)
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Integer id, Model model) {
        // Cần bổ sung hàm findById trong ReservationService nếu chưa có
        // model.addAttribute("res", reservationService.getById(id));
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
        return "booking/history"; // File này bạn tạo trong templates/booking/
    }
}