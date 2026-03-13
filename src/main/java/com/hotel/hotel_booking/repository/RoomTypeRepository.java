package com.hotel.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

}