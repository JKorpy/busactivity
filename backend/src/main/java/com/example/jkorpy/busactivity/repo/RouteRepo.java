package com.example.jkorpy.busactivity.repo;

import com.example.jkorpy.busactivity.models.Routes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepo extends JpaRepository<Routes, Object> {

    Optional<Routes> findByRouteShortName(String routeShortName);
}
