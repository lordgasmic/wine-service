package com.lordgasmic.wineservice.controller;

import com.lordgasmic.wineservice.models.WineryRequest;
import com.lordgasmic.wineservice.models.WineryResponse;
import com.lordgasmic.wineservice.service.WineryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@Slf4j
public class WineryController {

    private final WineryService service;

    public WineryController(WineryService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/wineries")
    public List<WineryResponse> getWineries() throws SQLException {
        return service.getWineries();
    }

    @GetMapping("/api/v1/wineries/{id}")
    public WineryResponse getWineryById(@PathVariable final String id) throws SQLException {
        return service.getWineryById(id);
    }

    @PutMapping("/api/v1/wineries")
    public WineryResponse addWinery(@RequestBody final WineryRequest wineryRequest) throws SQLException {
        return service.addWinery(wineryRequest);
    }
}
