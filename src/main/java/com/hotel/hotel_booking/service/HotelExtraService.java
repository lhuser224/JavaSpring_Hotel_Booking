package com.hotel.hotel_booking.service;

import com.hotel.hotel_booking.entity.ExtraService;
import com.hotel.hotel_booking.entity.ServicePackage;
import com.hotel.hotel_booking.repository.ExtraServiceRepository;
import com.hotel.hotel_booking.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class HotelExtraService {

    @Autowired private ExtraServiceRepository extraServiceRepo;
    @Autowired private ServicePackageRepository packageRepo;

    // Lấy các gói Standard/Deluxe 
    public List<ServicePackage> getAllPackages() {
        return packageRepo.findAll();
    }

    // Lấy các dịch vụ thêm như Airport Transfer, Spa 
    public List<ExtraService> getAllExtraServices() {
        return extraServiceRepo.findAll().stream()
                .filter(ExtraService::getIsActive)
                .toList();
    }
}