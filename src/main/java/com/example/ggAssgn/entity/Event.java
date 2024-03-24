package com.example.ggAssgn.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Data
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String cityName;
    private LocalDate date;
    private LocalTime time;
    private Double latitude;
    private Double longitude;
}
