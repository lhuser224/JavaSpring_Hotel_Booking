package com.hotel.hotel_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Extra_Services")
public class ExtraService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "service_name")
    private String serviceName;

    private String description;

    private Double price;

    private String imageUrl;

    private Boolean isActive;

    public ExtraService(){}

    public Integer getServiceId(){ return serviceId; }
    public void setServiceId(Integer serviceId){ this.serviceId = serviceId; }

    public String getServiceName(){ return serviceName; }
    public void setServiceName(String serviceName){ this.serviceName = serviceName; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }

    public Double getPrice(){ return price; }
    public void setPrice(Double price){ this.price = price; }

    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }

    public Boolean getIsActive(){ return isActive; }
    public void setIsActive(Boolean isActive){ this.isActive = isActive; }
}