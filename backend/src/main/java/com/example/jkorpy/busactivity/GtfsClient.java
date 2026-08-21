package com.example.jkorpy.busactivity;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GtfsClient {
    private final RestClient gtfsRestClient;

    public GtfsClient(RestClient gtfsRestClient) {
        this.gtfsRestClient = gtfsRestClient;
    }


    public FeedMessage fetchVehiclePositions() {
        byte [] body = gtfsRestClient.get()
                .uri("/Vehicles")
                .retrieve()
                .body(byte[].class);
        try {
            return FeedMessage.parseFrom(body);
        } catch(InvalidProtocolBufferException e) {
            throw new IllegalStateException("Failed to parse NTA-GTFS-Realtime Feed", e);

        }
    }
}
