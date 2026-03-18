package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.Reservation;
import com.hotel.hotel_booking.service.ReservationService;
import com.hotel.hotel_booking.service.RoomService;
import com.hotel.hotel_booking.service.HotelExtraService;
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

    // Hiển thị giao diện chọn phòng và dịch vụ 
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes()); // Lấy Single/Family [cite: 141-143]
        model.addAttribute("packages", hotelExtraService.getAllPackages()); // Lấy Standard/Deluxe [cite: 150-152]
        model.addAttribute("extraServices", hotelExtraService.getAllExtraServices()); // Lấy Airport/Spa [cite: 153-155]
        model.addAttribute("reservation", new Reservation());
        return "booking/create"; // Trả về file create.html trong folder templates/booking
    }

    // Xử lý lưu giỏ hàng tạm thời (Cart)
    @PostMapping("/save-cart")
    public String saveCart(@ModelAttribute("reservation") Reservation reservation) {
        Reservation saved = reservationService.createCart(reservation);
        return "redirect:/bookings/details/" + saved.getReservationId();
    }

    // Thêm dịch vụ đi kèm vào đơn hàng 
    @PostMapping("/{id}/add-addon")
    public String addAddon(@PathVariable Integer id, @RequestParam Integer serviceId, @RequestParam int qty) {
        reservationService.addExtraService(id, serviceId, qty);
        return "redirect:/bookings/details/" + id;
    }

    // Xác nhận đặt phòng và cấp Voucher 
    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Integer id, Model model) {
        Reservation res = reservationService.confirmBooking(id);
        model.addAttribute("reservation", res);
        return "booking/confirmation"; // Trang hiển thị mã Voucher
    }
}