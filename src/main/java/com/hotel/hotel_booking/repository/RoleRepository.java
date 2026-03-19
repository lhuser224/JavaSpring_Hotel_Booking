package com.hotel.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}