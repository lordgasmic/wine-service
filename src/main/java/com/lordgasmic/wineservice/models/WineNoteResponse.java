package com.lordgasmic.wineservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class WineNoteResponse {
    List<WineNoteOutput> wineNotes;

    public WineNoteResponse() {
        wineNotes = new ArrayList<>();
    }
}
