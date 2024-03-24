package com.example.ggAssgn.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {
    public String eventName;
    public String cityName;
    public LocalDate date;
    public String weather;
    public Double distance;
}
