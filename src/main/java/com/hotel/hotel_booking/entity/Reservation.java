package com.hotel.hotel_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Integer guestCount;

    private String status;

    private String confirmationVoucher;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    public Reservation(){}

}