package com.example.jkorpy.busactivity.dtos;

public record VehicleDto(
        String vehicleId,
        String tripId,
        String routeId,
        double latitude,
        double longitude,
        float bearing,
        long timestamp
){}
