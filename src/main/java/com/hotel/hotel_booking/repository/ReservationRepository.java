package com.hotel.hotel_booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	List<Reservation> findByUser_UserIdAndStatus(Integer userId, String status);
	List<Reservation> findByUser_UserIdAndStatusNotOrderByReservationIdDesc(Integer userId, String status);
}