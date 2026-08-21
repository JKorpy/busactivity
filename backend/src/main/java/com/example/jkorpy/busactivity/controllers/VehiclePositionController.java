package com.example.jkorpy.busactivity.controllers;

import com.example.jkorpy.busactivity.dtos.VehiclePositionDto;
import com.example.jkorpy.busactivity.services.VehiclePositionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehiclePositionController {

    private final VehiclePositionService vehicleService;

    public VehiclePositionController(VehiclePositionService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehiclePositionDto> getVehicles() {
        return vehicleService.getVehiclePosition();
    }

}
