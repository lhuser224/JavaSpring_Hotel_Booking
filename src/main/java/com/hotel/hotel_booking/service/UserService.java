package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.Role;
import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.repository.UserRepository;
import com.hotel.hotel_booking.repository.RoleRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired 
    private UserRepository userRepo;

    @Autowired 
    private RoleRepository roleRepo;

    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    // call in reservation controller
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role defaultRole = roleRepo.findByRoleName("ROLE_USER");
        if (defaultRole != null) {
            user.getRoles().add(defaultRole);
        }
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public User updateUserInfo(User user) {
        return userRepo.save(user); 
    }
    
    public void changePassword(String username, String oldPass, String newPass) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userRepo.save(user);
    }
}