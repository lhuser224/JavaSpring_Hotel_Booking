package com.hotel.hotel_booking.config;

import com.hotel.hotel_booking.entity.Role;
import com.hotel.hotel_booking.entity.User;
import com.hotel.hotel_booking.repository.RoleRepository;
import com.hotel.hotel_booking.repository.UserRepository;
import com.hotel.hotel_booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserService userService, 
                                   UserRepository userRepo, 
                                   RoleRepository roleRepo) {
        return args -> {
            if (roleRepo.findByRoleName("ROLE_ADMIN") == null) {
                Role adminRole = new Role();
                adminRole.setRoleName("ROLE_ADMIN");
                roleRepo.save(adminRole);
            }
            if (roleRepo.findByRoleName("ROLE_USER") == null) {
                Role userRole = new Role();
                userRole.setRoleName("ROLE_USER");
                roleRepo.save(userRole);
            }

            if (userRepo.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setFullName("System Administrator");
                admin.setIdPassportNumber("000000000");
                admin.setPhone("000000000");
                
                userService.registerUser(admin);

                User savedAdmin = userRepo.findByUsername("admin");
                Role adminRole = roleRepo.findByRoleName("ROLE_ADMIN");
                if (savedAdmin.getRoles() == null) {
                    savedAdmin.setRoles(new HashSet<>());
                }
                savedAdmin.getRoles().add(adminRole);
                userRepo.save(savedAdmin);
            }
        };
    }
}