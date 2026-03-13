package com.hotel.hotel_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.hotel_booking.entity.ReservationAddOn;

public interface ReservationAddOnRepository extends JpaRepository<ReservationAddOn, Integer> {

}