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
    //Update/Create loại phòng
    @PostMapping("/types/save")
    public String saveRoomType(@ModelAttribute RoomType roomType, RedirectAttributes ra) {
        try {
            // Hibernate sẽ tự động INSERT nếu ID null, hoặc UPDATE nếu ID đã tồn tại
            roomService.saveRoomType(roomType); 
            
            String msg = (roomType.getRoomTypeId() == null) ? "Thêm mới" : "Cập nhật";
            ra.addFlashAttribute("success", msg + " loại phòng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi lưu dữ liệu: " + e.getMessage());
        }
        return "redirect:/admin/rooms/manage";
    }
    @GetMapping("/manage")
    public String listRooms(Model model) {
        // Gửi cả 2 danh sách sang để hiển thị 2 bảng
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("roomTypes", roomService.getAllRoomTypes()); 
        return "admin/room-manage";
    }

    // cập nhật giá của RoomType
    @PostMapping("/types/update-price")
    public String updateTypePrice(@RequestParam("typeId") Integer typeId, 
                                 @RequestParam("newPrice") Double newPrice,
                                 RedirectAttributes ra) {
        RoomType type = roomService.getRoomTypeById(typeId);
        if (type != null) {
            type.setBasePrice(newPrice);
            roomService.saveRoomType(type);
            ra.addFlashAttribute("success", "Đã cập nhật giá mới cho loại phòng " + type.getTypeName());
        }
        return "redirect:/admin/rooms/manage";
    }

    // Soft Delete: Vô hiệu hóa phòng
    @PostMapping("/toggle/{id}")
    public String toggleRoom(@PathVariable Integer id) {
        roomService.toggleRoomStatus(id);
        return "redirect:/admin/rooms/manage";
    }
    
    
}