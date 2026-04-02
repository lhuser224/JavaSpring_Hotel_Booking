package com.hotel.hotel_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	List<Room> findByRoomType_RoomTypeIdAndIsActiveTrue(Integer roomTypeId);
}