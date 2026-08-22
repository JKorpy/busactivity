package com.example.jkorpy.busactivity.controllers;

import com.example.jkorpy.busactivity.dtos.VehiclePositionDto;
import com.example.jkorpy.busactivity.services.VehiclePositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VehiclePositionController {

    private final VehiclePositionService vehicleService;

    public VehiclePositionController(VehiclePositionService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/gtfs")
    public List<VehiclePositionDto> getVehicles() {
        return vehicleService.getVehiclePosition();
    }

    @GetMapping("/redis/{routeId}")
    public ResponseEntity<?> getRedisData(@PathVariable String routeId) {

        List<VehiclePositionDto> routes =
                vehicleService.getVehicleByRoute(routeId);

        if (routes.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Route: "+routeId+" not found in Redis");
        }

        return ResponseEntity.ok(routes);
    }

}
