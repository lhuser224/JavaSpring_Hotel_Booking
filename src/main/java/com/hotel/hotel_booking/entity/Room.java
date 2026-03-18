package com.hotel.hotel_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    public Room(){}

    public Integer getRoomId(){ return roomId; }
    public void setRoomId(Integer roomId){ this.roomId = roomId; }

    public String getRoomNumber(){ return roomNumber; }
    public void setRoomNumber(String roomNumber){ this.roomNumber = roomNumber; }

    public Boolean getIsActive(){ return isActive; }
    public void setIsActive(Boolean isActive){ this.isActive = isActive; }

    public RoomType getRoomType(){ return roomType; }
    public void setRoomType(RoomType roomType){ this.roomType = roomType; }
}