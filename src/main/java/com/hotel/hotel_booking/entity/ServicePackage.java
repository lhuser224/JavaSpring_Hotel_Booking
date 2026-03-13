package com.hotel.hotel_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Service_Packages")
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "package_name")
    private String packageName;

    private Boolean includesBreakfast;

    private Boolean allowsLateCheckout;

    private Double packagePrice;

    public ServicePackage(){}

    public Integer getPackageId(){ return packageId; }
    public void setPackageId(Integer packageId){ this.packageId = packageId; }

    public String getPackageName(){ return packageName; }
    public void setPackageName(String packageName){ this.packageName = packageName; }

    public Boolean getIncludesBreakfast(){ return includesBreakfast; }
    public void setIncludesBreakfast(Boolean includesBreakfast){ this.includesBreakfast = includesBreakfast; }

    public Boolean getAllowsLateCheckout(){ return allowsLateCheckout; }
    public void setAllowsLateCheckout(Boolean allowsLateCheckout){ this.allowsLateCheckout = allowsLateCheckout; }

    public Double getPackagePrice(){ return packagePrice; }
    public void setPackagePrice(Double packagePrice){ this.packagePrice = packagePrice; }
}