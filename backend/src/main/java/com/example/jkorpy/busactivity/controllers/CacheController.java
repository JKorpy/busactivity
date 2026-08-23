package com.example.jkorpy.busactivity.controllers;

import com.example.jkorpy.busactivity.dtos.VehicleDto;
import com.example.jkorpy.busactivity.services.CacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CacheController {
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/redis/{routeId}")
    public ResponseEntity<?> getRoute(@PathVariable String routeId) {

        List<VehicleDto> routes = cacheService.getVehicleByRoute(routeId);
        if (routes.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Route: "+routeId+" not found in Redis");
        }
        return ResponseEntity.ok(routes);
    }

}
