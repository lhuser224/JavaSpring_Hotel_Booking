package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.entity.RoomType;
import com.hotel.hotel_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RoomService roomService; 

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            Model model) {

        List<RoomType> roomTypes = roomService.searchAvailableRoomTypes(typeName, maxPrice, checkIn, checkOut);
        
        model.addAttribute("roomTypes", roomTypes);
        
        model.addAttribute("typeName", typeName);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        return "index"; 
    }
}