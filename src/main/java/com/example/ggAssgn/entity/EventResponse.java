package com.example.ggAssgn.entity;

import lombok.Data;

import java.util.List;

@Data
public class EventResponse {
    private List<EventDTO> events;
    private int page;
    private int pageSize;
    private int totalEvents;
    private int totalPages;
}
