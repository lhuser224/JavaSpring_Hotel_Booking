package com.hotel.hotel_booking.controller;

import com.hotel.hotel_booking.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private RoomTypeRepository roomTypeRepo;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("roomTypes", roomTypeRepo.findAll());
        return "index"; 
    }
}