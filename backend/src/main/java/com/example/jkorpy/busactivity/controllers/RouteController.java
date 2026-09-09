package com.example.jkorpy.busactivity.controllers;

import com.example.jkorpy.busactivity.dtos.VehicleDto;
import com.example.jkorpy.busactivity.models.Routes;
import com.example.jkorpy.busactivity.services.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getActiveRoutes() {
        List<VehicleDto> routes = routeService.getActiveVehicles();
        if(routes.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get route list");
        }
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<?> getRouteById(@PathVariable String routeId) {
        List<VehicleDto> routes = routeService.getVehicleByRoute(routeId);
        if (routes.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Route: "+routeId+" does not exist");
        }
        return ResponseEntity.ok(routes);
    }




}
