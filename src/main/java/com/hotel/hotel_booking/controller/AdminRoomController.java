package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.Room;
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
    /**
     * Lưu hoặc cập nhật loại phòng.
     * Xử lý trường hợp ID truyền lên là chuỗi rỗng từ form.
     */
    @PostMapping("/types/save")
    public String saveRoomType(@ModelAttribute RoomType roomType, RedirectAttributes ra) {
        try {
            // Kiểm tra nếu ID <= 0 (do gán "" từ JS) thì đặt về null để Hibernate thực hiện INSERT
            if (roomType.getRoomTypeId() != null && roomType.getRoomTypeId() <= 0) {
                roomType.setRoomTypeId(null);
            }
            
            roomService.saveRoomType(roomType);
            
            String msg = (roomType.getRoomTypeId() == null) ? "Thêm mới" : "Cập nhật";
            ra.addFlashAttribute("success", msg + " loại phòng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi lưu dữ liệu: " + e.getMessage());
        }
        return "redirect:/admin/rooms/manage";
    }

    /**
     * Thêm một phòng vật lý mới và gán vào một Loại phòng cụ thể.
     */
    @PostMapping("/add")
    public String addRoom(@RequestParam("roomNumber") String roomNumber, 
                          @RequestParam("roomTypeId") Integer roomTypeId,
                          RedirectAttributes ra) {
        try {
            Room newRoom = new Room();
            newRoom.setRoomNumber(roomNumber);
            newRoom.setIsActive(true);
            
            // Liên kết với RoomType thông qua ID
            RoomType type = roomService.getRoomTypeById(roomTypeId);
            newRoom.setRoomType(type);
            
            roomService.saveRoom(newRoom);
            ra.addFlashAttribute("success", "Đã thêm phòng " + roomNumber);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi thêm phòng: " + e.getMessage());
        }
        return "redirect:/admin/rooms/manage";
    }
    // Hiển thị danh sách loại phòng để quản lý giá/mô tả
    @GetMapping("/types")
    public String listRoomTypes(Model model) {
        model.addAttribute("roomTypes", roomService.getAllRoomTypes());
        return "admin/room-type-list";
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