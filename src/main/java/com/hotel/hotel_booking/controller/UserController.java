package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.service.UserService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users") // Tiền tố cho tất cả các route trong controller này (ví dụ: /users/register)
public class UserController {

    @Autowired 
    private UserService userService;

    /**
     * Chức năng: Hiển thị trang đăng ký
     * Cơ chế: Tạo một đối tượng User rỗng đẩy sang View để Thymeleaf bind dữ liệu vào form
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Chức năng: Xử lý lưu người dùng mới
     * Luồng đi: Nhận data từ Form -> Gửi sang Service để mã hóa mật khẩu & lưu DB -> Điều hướng về Login
     */
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

    /**
     * Chức năng: Hiển thị trang đổi mật khẩu
     * Điều kiện: Yêu cầu người dùng phải đăng nhập mới truy cập được (Cấu hình trong Security)
     */
    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "change-password";
    }

    /**
     * Chức năng: Xử lý thay đổi mật khẩu
     * @param oldPass: Mật khẩu cũ nhận từ request param
     * @param newPass: Mật khẩu mới nhận từ request param
     * @param principal: Đối tượng của Spring Security chứa thông tin user đang đăng nhập hiện tại
     * @param ra: Dùng để truyền thông báo (FlashAttribute) sau khi redirect trang
     */
    @PostMapping("/change-password")
    public String handleChangePassword(@RequestParam String oldPass, 
                                       @RequestParam String newPass, 
                                       Principal principal, 
                                       RedirectAttributes ra) {
        try {
            // Ủy thác toàn bộ logic kiểm tra và mã hóa cho lớp Service
            userService.changePassword(principal.getName(), oldPass, newPass);
            
            // Nếu thành công, gửi thông báo xanh
            ra.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            // Nếu có lỗi (sai mật khẩu cũ), gửi thông báo đỏ lấy từ thông điệp của Service
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users/change-password";
    }
}