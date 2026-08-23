package com.example.jkorpy.busactivity.services;

import com.example.jkorpy.busactivity.client.GtfsClient;
import com.example.jkorpy.busactivity.dtos.VehicleDto;
import com.example.jkorpy.busactivity.mapper.VehiclePositionMapper;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GtfsService {
    private final GtfsClient gtfsClient;
    private final VehiclePositionMapper vehicleMapper;

    public GtfsService(
            GtfsClient gtfsRestClient,
            VehiclePositionMapper vehicleMapper
    ) {
        this.gtfsClient = gtfsRestClient;
        this.vehicleMapper = vehicleMapper;
    }

    @CachePut(value = "vehicles", key="'all'")
    public List<VehicleDto> refreshVehicles() {
        return gtfsClient.fetchNtaData("/Vehicles")
                .getEntityList()
                .stream()
                .filter(FeedEntity::hasVehicle)
                .map(vehicleMapper::toDto)
                .toList();
    }

    @Cacheable(value = "vehicles", key="'all'")
    public List<VehicleDto> getVehicles() {
        return refreshVehicles();
    }
}
