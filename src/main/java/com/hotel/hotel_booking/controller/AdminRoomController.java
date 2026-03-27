package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.RoomType;
import com.hotel.hotel_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    @Autowired private RoomService roomService;

    // Hiển thị danh sách loại phòng để quản lý giá/mô tả
    @GetMapping("/types")
    public String listRoomTypes(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "admin/room-type-list";
    }

    // Lưu cập nhật loại phòng (Update)
    @PostMapping("/types/save")
    public String saveType(@ModelAttribute RoomType roomType, RedirectAttributes ra) {
        roomService.saveRoomType(roomType);
        ra.addFlashAttribute("success", "Cập nhật loại phòng thành công!");
        return "redirect:/admin/rooms/types";
    }

    // Quản lý từng phòng cụ thể (CRUD + Soft Delete)
    @GetMapping("/manage")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/room-manage";
    }

    // Soft Delete: Vô hiệu hóa phòng
    @PostMapping("/toggle/{id}")
    public String toggleRoom(@PathVariable Integer id) {
        roomService.toggleRoomStatus(id);
        return "redirect:/admin/rooms/manage";
    }
    
    
}