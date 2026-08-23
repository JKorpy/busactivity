package com.example.jkorpy.busactivity.services;

import com.example.jkorpy.busactivity.dtos.VehicleDto;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {
    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public List<VehicleDto> getVehicleByRoute(String routeId) {
        if (routeId == null || routeId.isBlank()) {
            return List.of();
        }
        return getCacheVehicle()
                .stream()
                .filter(v -> {
                    String[] parts = v.routeId().trim().split("\\s+"); //Scuffed, should refactor
                    return parts.length > 1 && routeId.equals(parts[1]);
                })
                .toList();
    }

    private List<VehicleDto> getCacheVehicle(){
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
        List<VehicleDto> vehicles = (List<VehicleDto>) wrapper.get();
        return vehicles;

    }
}
