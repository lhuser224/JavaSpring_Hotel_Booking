package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired private UserRepository userRepo;

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }
    public User createOrUpdateUser(User user) {
        return userRepo.save(user); // Lưu thông tin ID/Passport, Phone 
    }    
}