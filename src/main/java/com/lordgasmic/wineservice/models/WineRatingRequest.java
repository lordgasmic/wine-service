package com.lordgasmic.wineservice.models;

import lombok.Data;

@Data
public class WineRatingRequest {
    private int wineId;
    private String user;
    private String date;
    private String rating;
}
