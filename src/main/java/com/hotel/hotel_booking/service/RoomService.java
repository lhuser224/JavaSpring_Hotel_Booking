package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.Room;
import com.hotel.hotel_booking.entity.RoomType;
import com.hotel.hotel_booking.repository.RoomRepository;
import com.hotel.hotel_booking.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RoomService {

    @Autowired private RoomRepository roomRepo;
    @Autowired private RoomTypeRepository roomTypeRepo;
    // gọi từ repo roomtype
    public List<RoomType> searchAvailableRoomTypes(String typeName, Double maxPrice, LocalDate checkIn, LocalDate checkOut) {
        return roomTypeRepo.searchAvailableRoomTypes(typeName, maxPrice, checkIn, checkOut);
    }
    // Lấy danh sách loại phòng (Single/Family) để User chọn 
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepo.findAll();
    }

    // Lấy danh sách các phòng đang hoạt động
    public List<Room> getAvailableRooms() {
        return roomRepo.findAll().stream()
                .filter(Room::getIsActive)
                .toList();
    }
    
    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng với ID: " + id));
    }
}