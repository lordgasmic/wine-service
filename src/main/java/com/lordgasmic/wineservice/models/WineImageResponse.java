package com.lordgasmic.wineservice.models;

import lombok.Data;

import java.util.List;

@Data
public class WineImageResponse {
    private List<WineImage> wineImages;
}
