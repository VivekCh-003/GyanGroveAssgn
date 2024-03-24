package com.example.ggAssgn.service;

import com.example.ggAssgn.entity.EventDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    void createEvents(MultipartFile file);

    List<EventDTO> findEvents(Double latitude, Double longitude, LocalDate date);
}
