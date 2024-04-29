package com.lordgasmic.wineservice.controller;

import com.lordgasmic.wineservice.models.WineRequest;
import com.lordgasmic.wineservice.models.WineResponse;
import com.lordgasmic.wineservice.service.WineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@Slf4j
public class WineController {

    private final WineService service;

    public WineController(WineService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/wines")
    public Object getWines(@RequestParam("wineId") final Optional<String> wineId,
                           @RequestParam("wineryId") final Optional<String> wineryId) throws SQLException {
        if (wineId.isEmpty() && wineryId.isEmpty()) {
            return service.getAllWines();
        } else if (wineId.isPresent()) {
            System.out.println("wineid: " + wineId.get());
            return service.getWine(wineId.get());
        } else {
            return service.getWinesByWineryId(wineryId.get());
        }
    }

    @PutMapping("/api/v1/wines")
    public WineResponse addWine(@RequestBody final WineRequest wineRequest) throws SQLException {
        return service.addWine(wineRequest);
    }
}
