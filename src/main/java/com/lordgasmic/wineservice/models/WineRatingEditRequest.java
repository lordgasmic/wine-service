package com.lordgasmic.wineservice.models;

import lombok.Data;

@Data
public class WineRatingEditRequest {
    private String id;
    private String rating;
}
