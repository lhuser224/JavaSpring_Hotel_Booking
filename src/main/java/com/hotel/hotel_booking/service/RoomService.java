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

    @Autowired 
    private RoomRepository roomRepo;

    @Autowired 
    private RoomTypeRepository roomTypeRepo;

    // ROOM TYPE

    // Lấy tất cả loại phòng
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepo.findAll();
    }

    // Lấy theo ID
    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng với ID: " + id));
    }

    // Tạo / Update RoomType
    public void saveRoomType(RoomType roomType) {
        roomTypeRepo.save(roomType);
    }

    // ROOM

    // Lấy tất cả phòng (kể cả inactive)
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    // Chỉ lấy phòng đang active
    public List<Room> getAvailableRooms() {
        return roomRepo.findAll()
                .stream()
                .filter(Room::getIsActive)
                .toList();
    }

    // Lấy phòng theo ID
    public Room getRoomById(Integer id) {
        return roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + id));
    }

    // Tạo / Update Room
    public void saveRoom(Room room) {
        roomRepo.save(room);
    }

    // Soft Delete (toggle active)
    public void toggleRoomStatus(Integer id) {
        Room room = getRoomById(id);
        room.setIsActive(!room.getIsActive());
        roomRepo.save(room);
    }
    // SEARCH / BUSINESS LOGIC

    // Search loại phòng còn trống theo điều kiện
    public List<RoomType> searchAvailableRoomTypes(
            String typeName,
            Double maxPrice,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return roomTypeRepo.searchAvailableRoomTypes(typeName, maxPrice, checkIn, checkOut);
    }
}