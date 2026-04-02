package com.hotel.hotel_booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotel.hotel_booking.entity.Reservation;
import com.hotel.hotel_booking.entity.Room;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	List<Reservation> findByUser_UserIdAndStatus(Integer userId, String status);
	List<Reservation> findByUser_UserIdAndStatusNotOrderByReservationIdDesc(Integer userId, String status);
	@Query("SELECT r FROM Room r " +
		       "WHERE r.roomType.roomTypeId = :typeId " +
		       "AND r.isActive = true " +
		       "AND NOT EXISTS (" +
		       "    SELECT 1 FROM Reservation res " +
		       "    WHERE res.room.roomId = r.roomId " +
		       "    AND res.status <> 'Cancelled' " +
		       "    AND res.checkInDate < :checkOut " +
		       "    AND res.checkOutDate > :checkIn" +
		       ")")
		List<Room> findAvailablePhysicalRooms(@Param("typeId") Integer typeId, 
		                                     @Param("checkIn") LocalDate checkIn, 
		                                     @Param("checkOut") LocalDate checkOut);
}