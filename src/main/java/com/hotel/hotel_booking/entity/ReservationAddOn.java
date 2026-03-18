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

    public Integer getAddOnId() {
        return addOnId;
    }

    public void setAddOnId(Integer addOnId) {
        this.addOnId = addOnId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ExtraService getExtraService() {
        return extraService;
    }

    public void setExtraService(ExtraService extraService) {
        this.extraService = extraService;
    }
    
    @Column(name = "price_at_booking")
    private Double priceAtBooking;

    public Double getPriceAtBooking() {
        return priceAtBooking;
    }

    public void setPriceAtBooking(Double priceAtBooking) {
        this.priceAtBooking = priceAtBooking;
    }
}