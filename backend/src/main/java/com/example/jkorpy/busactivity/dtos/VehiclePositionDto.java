package com.example.jkorpy.busactivity.dtos;

public record VehiclePositionDto(
        String vehicleId,
        String tripId,
        String routeId,
        double latitude,
        double longitude,
        float bearing,
        long timestamp
){}
