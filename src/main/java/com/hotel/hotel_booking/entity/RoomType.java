package com.hotel.hotel_booking.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Room_Types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "bed_description")
    private String bedDescription;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "image_url")
    private String imageUrl;

    public RoomType(){}

    public Integer getRoomTypeId(){ return roomTypeId; }
    public void setRoomTypeId(Integer roomTypeId){ this.roomTypeId = roomTypeId; }

    public String getTypeName(){ return typeName; }
    public void setTypeName(String typeName){ this.typeName = typeName; }

    public String getBedDescription(){ return bedDescription; }
    public void setBedDescription(String bedDescription){ this.bedDescription = bedDescription; }

    public Double getBasePrice(){ return basePrice; }
    public void setBasePrice(Double basePrice){ this.basePrice = basePrice; }

    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
    
    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}