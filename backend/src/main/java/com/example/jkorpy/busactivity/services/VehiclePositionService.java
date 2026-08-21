package com.example.jkorpy.busactivity.services;

import com.example.jkorpy.busactivity.GtfsClient;
import com.example.jkorpy.busactivity.dtos.VehiclePositionDto;
import com.example.jkorpy.busactivity.mapper.VehiclePositionMapper;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiclePositionService {


    private final GtfsClient gtfsClient;
    private final VehiclePositionMapper vehicleMapper;

    public VehiclePositionService(GtfsClient gtfsRestClient, VehiclePositionMapper vehicleMapper) {
        this.gtfsClient = gtfsRestClient;
        this.vehicleMapper = vehicleMapper;
    }

    public List<VehiclePositionDto> getVehiclePosition() {
        return gtfsClient.fetchVehiclePositions()
                .getEntityList()
                .stream()
                .filter(FeedEntity::hasVehicle)
                .map(vehicleMapper::toDto)
                .toList();
    }
}
