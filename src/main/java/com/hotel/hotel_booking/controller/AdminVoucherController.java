package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.Reservation;
import com.hotel.hotel_booking.service.ReservationService;
import com.hotel.hotel_booking.repository.ReservationRepository; // Cần để update status nhanh hoặc dùng Service nếu có method
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {

    @Autowired 
    private ReservationService reservationService;

    // Tiêm trực tiếp Repo để update status
    @Autowired
    private ReservationRepository reservationRepo;

    /**
     * Hiển thị danh sách Voucher
     * @param keyword: Tìm theo tên khách hoặc mã Voucher
     * @param status: Lọc theo trạng thái (Confirmed, Checked_In, Checked_Out, Cancelled)
     */
    @GetMapping
    public String listVouchers(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "status", required = false) String status, 
                               Model model) {
        
        // Sử dụng method searchReservations có sẵn trong Service của bạn
        List<Reservation> list = reservationService.searchReservations(keyword, status);
        
        // Lọc bỏ trạng thái "Cart" để chỉ hiện Voucher thực tế
        List<Reservation> voucherList = list.stream()
                .filter(r -> !"Cart".equalsIgnoreCase(r.getStatus()))
                .toList();

        model.addAttribute("vouchers", voucherList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentStatus", status); // Để active các Tab lọc
        
        return "admin/voucher-list";
    }

    /**
     * Xác nhận Check-in
     */
    @PostMapping("/check-in/{id}")
    public String checkIn(@PathVariable Integer id, RedirectAttributes ra) {
        Reservation res = reservationService.getById(id);
        if ("Confirmed".equalsIgnoreCase(res.getStatus())) {
            res.setStatus("Checked_In");
            reservationRepo.save(res); // Lưu cập nhật
            ra.addFlashAttribute("success", "Khách đã nhận phòng thành công!");
        } else {
            ra.addFlashAttribute("error", "Trạng thái không hợp lệ để Check-in");
        }
        return "redirect:/admin/vouchers?status=Checked_In";
    }

    /**
     * Xác nhận Check-out
     */
    @PostMapping("/check-out/{id}")
    public String checkOut(@PathVariable Integer id, RedirectAttributes ra) {
        Reservation res = reservationService.getById(id);
        if ("Checked_In".equalsIgnoreCase(res.getStatus())) {
            res.setStatus("Checked_Out");
            reservationRepo.save(res);
            ra.addFlashAttribute("success", "Khách đã trả phòng & hoàn tất đơn!");
        }
        return "redirect:/admin/vouchers?status=Checked_Out";
    }

    /**
     * Hủy Voucher
     */
    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Integer id, RedirectAttributes ra) {
        Reservation res = reservationService.getById(id);
        res.setStatus("Cancelled");
        reservationRepo.save(res);
        ra.addFlashAttribute("success", "Đã hủy đơn đặt phòng này.");
        return "redirect:/admin/vouchers";
    }
}