package com.example.jkorpy.busactivity.services;

import com.example.jkorpy.busactivity.GtfsClient;
import com.example.jkorpy.busactivity.dtos.VehiclePositionDto;
import com.example.jkorpy.busactivity.mapper.VehiclePositionMapper;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiclePositionService {

    private final GtfsClient gtfsClient;
    private final VehiclePositionMapper vehicleMapper;
    private final CacheManager cacheManager;

    public VehiclePositionService(GtfsClient gtfsRestClient, VehiclePositionMapper vehicleMapper, CacheManager cacheManager) {
        this.gtfsClient = gtfsRestClient;
        this.vehicleMapper = vehicleMapper;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "vehicles", key="'all'")
    public List<VehiclePositionDto> getVehiclePosition() {
        return gtfsClient.fetchVehiclePositions()
                .getEntityList()
                .stream()
                .filter(FeedEntity::hasVehicle)
                .map(vehicleMapper::toDto)
                .toList();
    }


    public List<VehiclePositionDto> getVehicleByRoute(String routeId) {
        if (routeId == null || routeId.isBlank()) {
            return List.of();
        }

        return getCacheVehicle()
                .stream()
                .filter(v -> {
                    String[] parts = v.routeId().trim().split("\\s+"); //Scuffed, should refactor

                    return parts.length > 1
                            && routeId.equals(parts[1]);
                })
                .toList();
    }


    private List<VehiclePositionDto> getCacheVehicle(){

        //Using ValueWrapper to return the data with
        //the correct object type (not List, but List<VehiclePositionDto>)

        Cache cache = cacheManager.getCache("vehicles");
        if (cache == null) {
            return List.of();
        }

        Cache.ValueWrapper wrapper = cache.get("all");
        if (wrapper == null) {
            return List.of();
        }

        @SuppressWarnings("unchecked")
        List<VehiclePositionDto> vehicles =
                (List<VehiclePositionDto>) wrapper.get();

        return vehicles;

    }
}
