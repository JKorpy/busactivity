package com.example.jkorpy.busactivity.services;

import com.example.jkorpy.busactivity.dtos.VehicleDto;
import com.example.jkorpy.busactivity.models.Routes;
import com.example.jkorpy.busactivity.repo.RouteRepo;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final CacheManager cacheManager;
    private final RouteRepo routeRepo;

    public RouteService(CacheManager cacheManager, RouteRepo routeRepo) {
        this.cacheManager = cacheManager;
        this.routeRepo = routeRepo;
    }

    public List<VehicleDto> getVehicleByRoute(String routeName) {
        if (routeName == null || routeName.isBlank()) return List.of();

        String routeId = routeRepo.findByRouteShortName(routeName)
                .map(Routes::getRouteId)
                .orElse(null);

        if(routeId == null) return List.of();

        return getCacheVehicle()
                .stream()
                .filter(v -> routeId.equals(v.routeId()))
                .toList();
    }

    public List<VehicleDto> getActiveVehicles() {
        return getCacheVehicle()
                .stream()
                .toList();
    }


    private List<VehicleDto> getCacheVehicle(){
        //Using ValueWrapper to return the data with
        //the correct object type (not List, but List<VehiclePositionDto>)
        Cache cache = cacheManager.getCache("vehicles");
        if (cache == null) return List.of();

        Cache.ValueWrapper wrapper = cache.get("all");
        if (wrapper == null) return List.of();

        @SuppressWarnings("unchecked")
        List<VehicleDto> vehicles = (List<VehicleDto>) wrapper.get();
        return vehicles;
    }

    public List<Routes> getAllVehicles() {
        return routeRepo.findAll();
    }
}
