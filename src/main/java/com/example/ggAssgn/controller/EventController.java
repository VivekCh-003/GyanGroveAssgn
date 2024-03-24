package com.example.ggAssgn.controller;

import com.example.ggAssgn.entity.Event;
import com.example.ggAssgn.entity.EventDTO;
import com.example.ggAssgn.entity.EventResponse;
import com.example.ggAssgn.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(value = "/uploadCSV",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        try{
            eventService.createEvents(file);

            return ResponseEntity.ok("Events created successfully");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create events.");
        }
    }

    @GetMapping("/api/events/find")
    public ResponseEntity<EventResponse> findEvents(@RequestParam Double latitude, @RequestParam Double longitude ,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date){
        int page = 1;
        int pageSize = 10;

        List<EventDTO> events = eventService.findEvents(latitude,longitude,date);

        int totalEvents = events.size();
        int totalPages = (int) Math.ceil((double) totalEvents / pageSize);

        EventResponse response = new EventResponse();
        response.setEvents(events);
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setTotalEvents(totalEvents);
        response.setTotalPages(totalPages);


        return ResponseEntity.ok(response);
    }
}
