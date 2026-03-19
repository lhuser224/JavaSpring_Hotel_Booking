package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired 
    private UserService userService;

    // Hiển thị form đăng ký thông tin khách hàng
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Trả về templates/user/register.html
    }

    // Xử lý lưu thông tin khách hàng vào DB 
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/bookings/create";
    }
}