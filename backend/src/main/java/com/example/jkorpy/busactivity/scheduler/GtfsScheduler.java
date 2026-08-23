package com.example.jkorpy.busactivity.scheduler;

import com.example.jkorpy.busactivity.services.GtfsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class GtfsScheduler {
    private static final Logger log = LoggerFactory.getLogger(GtfsScheduler.class);

    private final GtfsService gtfsService;

    public GtfsScheduler(GtfsService gtfsService) {
        this.gtfsService = gtfsService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void refreshVehiclePositions() {
        try {
            gtfsService.refreshVehicles();
            log.info("Vehicles Cached");
        } catch (Exception e) {
            log.error("Failed to refresh vehicle positions", e);
        }
    }
}
