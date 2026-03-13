package com.hotel.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}