package com.lordgasmic.wineservice.controller;

import com.lordgasmic.wineservice.models.WineFriendsRequest;
import com.lordgasmic.wineservice.models.WineRatingEditRequest;
import com.lordgasmic.wineservice.models.WineRatingRequest;
import com.lordgasmic.wineservice.service.WineRatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@Slf4j
public class WineRatingController {

    private WineRatingService service;

    public WineRatingController(WineRatingService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/wineRating")
    public Object getWineRating(@RequestParam("user") final Optional<String> user,
                                @RequestParam("wineId") final Optional<Integer> wineId) throws SQLException {
        if (user.isEmpty() && wineId.isEmpty()) {
            return service.getAllWineRatings();
        } else if (wineId.isPresent() && user.isPresent()) {
            return service.getWineRatingsByWineIdByUser(wineId.get(), user.get());
        } else if (wineId.isPresent()) {
            return service.getWineRatingByWineId(wineId.get());
        } else {
            return service.getWineRatingsByUser(user.get());
        }
    }

    @PutMapping("/api/v1/wineRating")
    public Object addWineRating(@RequestBody final WineRatingRequest wineRatingRequest) throws SQLException {
        return service.addWineRating(wineRatingRequest);
    }

    @PostMapping("/api/v1/wineRating")
    public Object getWineRatingByUsersByWineIds(@RequestBody final WineFriendsRequest request) throws SQLException {
        return service.getWineRatingsByUsersByWineIds(request);
    }

    @PutMapping("/api/v1/wineRating/edit")
    public Object editWineRating(@RequestBody final WineRatingEditRequest request) throws SQLException {
        return service.editWineRating(request);
    }
}
