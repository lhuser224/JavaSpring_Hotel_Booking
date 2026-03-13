package com.hotel.hotel_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_AddOns")
public class ReservationAddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addOnId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ExtraService extraService;

    public ReservationAddOn(){}

}