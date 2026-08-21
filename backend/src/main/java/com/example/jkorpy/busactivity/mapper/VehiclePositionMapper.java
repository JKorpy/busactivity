package com.example.jkorpy.busactivity.mapper;

import com.example.jkorpy.busactivity.dtos.VehiclePositionDto;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import org.springframework.stereotype.Component;

@Component
public class VehiclePositionMapper {

    public VehiclePositionDto toDto(FeedEntity entity) {

        var vehiclePos = entity.getVehicle();
        var trip = vehiclePos.getTrip();
        var pos = vehiclePos.getPosition();
        var vehicle = vehiclePos.getVehicle();

        return new VehiclePositionDto(
                vehicle.getId(),
                trip.getTripId(),
                trip.getRouteId(),
                pos.getLatitude(),
                pos.getLongitude(),
                pos.getBearing(),
                vehiclePos.getTimestamp()
        );
    }
}
