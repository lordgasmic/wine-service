package com.lordgasmic.wineservice.controller;

import com.lordgasmic.wineservice.models.WineNoteRequest;
import com.lordgasmic.wineservice.service.WineNotesService;
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
public class WineNotesController {

    private WineNotesService service;

    public WineNotesController(WineNotesService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/wineNotes")
    public Object getWineNotes(@RequestParam("user") final Optional<String> user,
                               @RequestParam("wineId") final Optional<Integer> wineId) throws SQLException {
        if (user.isEmpty() && wineId.isEmpty()) {
            return service.getAllWineNotes();
        } else if (user.isPresent() && wineId.isPresent()) {
            return service.getWineNotesByWineIdByUser(wineId.get(), user.get());
        } else if (wineId.isPresent()) {
            return service.getWineNotesByWineId(wineId.get());
        } else {
            return service.getWineNotesByUser(user.get());
        }
    }

    @PutMapping("/api/v1/wineNotes")
    public Object addWineNotes(@RequestBody final WineNoteRequest wineNoteRequest) throws SQLException {
        return service.addWineNotes(wineNoteRequest);
    }
}
