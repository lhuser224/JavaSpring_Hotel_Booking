package com.hotel.hotel_booking.repository;

import com.hotel.hotel_booking.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    @Query("SELECT DISTINCT rt FROM RoomType rt " +
           "JOIN rt.rooms r " + 
           "WHERE r.isActive = true " +
           "AND (:typeName IS NULL OR LOWER(rt.typeName) LIKE LOWER(CONCAT('%', :typeName, '%'))) " +
           "AND (:maxPrice IS NULL OR rt.basePrice <= :maxPrice) " +
           "AND NOT EXISTS (" +
           "    SELECT 1 FROM Reservation res " +
           "    WHERE res.roomType = rt " + 
           "    AND res.status <> 'Cancelled' " +
           "    AND res.checkInDate < :checkOut " +
           "    AND res.checkOutDate > :checkIn" +
           ")")
    List<RoomType> searchAvailableRoomTypes(
            @Param("typeName") String typeName, 
            @Param("maxPrice") Double maxPrice, 
            @Param("checkIn") LocalDate checkIn, 
            @Param("checkOut") LocalDate checkOut,
            org.springframework.data.domain.Sort sort);
}