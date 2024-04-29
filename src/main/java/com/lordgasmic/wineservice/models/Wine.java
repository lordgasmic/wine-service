package com.lordgasmic.wineservice.models;

import lombok.Data;

@Data
public class Wine {
    private int id;
    private int wineryId;
    private String name;
    private String rating;
    private String date;
    private String style;
}
