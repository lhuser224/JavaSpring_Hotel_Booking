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
        // 1. Kiểm tra RoomType có tồn tại không để tránh lỗi 500
        RoomType selectedType = roomService.getRoomTypeById(typeId);
        if (selectedType == null) {
            return "redirect:/rooms"; // Hoặc trang báo lỗi
        }
        
        // 2. Khởi tạo đối tượng
        Reservation reservation = new Reservation();
        reservation.setRoomType(selectedType);
        reservation.setRoomQuantity(1);
        
        reservation.setGuestCount(1); 

        // 3. Xử lý Packages
        List<ServicePackage> packages = hotelExtraService.getAllPackages();
        
        ServicePackage defaultPkg = packages.stream()
                .filter(p -> "Standard".equalsIgnoreCase(p.getPackageName()))
                .findFirst()
                .orElse(packages.isEmpty() ? null : packages.get(0));
                
        reservation.setServicePackage(defaultPkg);

        // 4. Đưa dữ liệu sang View
        model.addAttribute("reservation", reservation);
        model.addAttribute("packages", packages);
        
        // hiển thị tiêu đề hoặc ảnh loại phòng dễ hơn
        model.addAttribute("selectedRoomType", selectedType);

        return "booking/create"; 
    }

    // 2. Lưu thông tin cơ bản vào giỏ hàng (Trạng thái "Cart")
    @PostMapping("/save-cart")
    public String saveCart(@ModelAttribute("reservation") Reservation reservation, Principal principal) {
        // 1. Kiểm tra đăng nhập
        if (principal == null) return "redirect:/login";

        // 2. Kiểm tra dữ liệu đầu vào cơ bản
        if (reservation.getRoomType() == null || reservation.getRoomType().getRoomTypeId() == null) {
            return "redirect:/";
        }

        // 3. Gán User hiện tại
        User currentUser = userService.findByUsername(principal.getName());
        reservation.setUser(currentUser);        

        /**
         * Nạp đầy đủ thông tin từ DB trước khi sang Service tính tiền.
         * Vì dữ liệu từ Form gửi lên thường chỉ có ID, các trường price sẽ bị null.
         */
        RoomType fullRoomType = roomService.getRoomTypeById(reservation.getRoomType().getRoomTypeId());
        
        // Kiểm tra thêm nếu có ServicePackage
        if (reservation.getServicePackage() != null && reservation.getServicePackage().getPackageId() != null) {
            ServicePackage fullPackage = hotelExtraService.getPackageById(reservation.getServicePackage().getPackageId());
            reservation.setServicePackage(fullPackage);
        }
        
        reservation.setRoomType(fullRoomType);

        // 4. Lưu giỏ hàng (Lúc này createCart gọi calculateAndSetTotal sẽ có giá tiền để tính)
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
    //Thêm dịch vụ Add-on
    @PostMapping("/add-service")
    public String addService(@RequestParam("resId") Integer resId, 
                             @RequestParam("serviceId") Integer serviceId, 
                             @RequestParam("quantity") Integer quantity) {
        
        reservationService.addExtraService(resId, serviceId, quantity);
        
        return "redirect:/bookings/details/" + resId;
    }
    // 4. Xác nhận đặt phòng -> Đổi trạng thái sang "Confirmed" và cấp Voucher
    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Integer id) {
        reservationService.confirmBooking(id);
        // Sau khi confirm, redirect sang trang voucher để tránh lỗi ấn F5 bị đặt lại đơn
        return "redirect:/bookings/voucher/" + id;
    }
    
    @GetMapping("/history")
    public String showHistory(Principal principal, Model model) {
        String username = principal.getName();
        List<Reservation> history = reservationService.getHistoryByUsername(username);
        
        model.addAttribute("historyList", history);
        return "booking/history"; 
    }
    
    @GetMapping("/voucher/{id}")
    public String showVoucher(@PathVariable Integer id, Model model) {
        Reservation res = reservationService.getById(id);
        
        // 1. Tính số đêm
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            res.getCheckInDate(), 
            res.getCheckOutDate()
        );
        if (nights <= 0) nights = 1; 

        // 2. Tính toán các thành phần giá để gửi sang View
        double roomBasePrice = res.getRoomType().getBasePrice();
        double packagePrice = (res.getServicePackage() != null) ? res.getServicePackage().getPackagePrice() : 0;
        double roomSubtotal = (roomBasePrice + packagePrice) * res.getRoomQuantity() * nights;

        model.addAttribute("reservation", res);
        model.addAttribute("nights", nights);
        model.addAttribute("roomSubtotal", roomSubtotal);
        
        return "booking/voucher"; 
    }
    
}