package com.example.ggAssgn.service;

import com.example.ggAssgn.entity.Event;
import com.example.ggAssgn.entity.EventDTO;
import com.example.ggAssgn.repository.EventRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${distance.api.url}")
    private String distanceApiUrl;


    @Override
    public void createEvents(MultipartFile file){
        List<Event> events = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null){
                if (isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String values[] = line.split(",");
                Event event = new Event();
                event.setEventName(values[0]);
                event.setCityName(values[1]);
                event.setDate(LocalDate.parse(values[2], DateTimeFormatter.ofPattern("d-M-yyyy")));
                event.setTime(LocalTime.parse(values[3], DateTimeFormatter.ofPattern("H:m:s")));
                event.setLatitude(Double.parseDouble(values[4]));
                event.setLongitude(Double.parseDouble(values[5]));

                events.add(event);
            }
            eventRepository.saveAll(events);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<EventDTO> findEvents(Double latitude, Double longitude, LocalDate date) {
        LocalDate startDate = date;
        LocalDate endDate = date.plusDays(14);

        List<Event> events = eventRepository.findByDateBetween(startDate,endDate);


        List<EventDTO> eventDTOS = events.parallelStream()
                .map(event -> {
                    EventDTO eventDTO = convertEventToDTO(event);
                    String weather = null;
                    weather = String.valueOf(getWeather(event.getCityName(),event.getDate()));
                    Double distance = getDistance(latitude,longitude,event.getLatitude(),event.getLongitude());
                    eventDTO.setWeather(weather);
                    eventDTO.setDistance(Double.parseDouble(String.valueOf(distance)));
                    return eventDTO;
                })
                .collect(Collectors.toList());

        eventDTOS.sort(Comparator.comparing(EventDTO::getDate));
        return eventDTOS;

    }


    private EventDTO convertEventToDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName(event.getEventName());
        eventDTO.setCityName(event.getCityName());
        eventDTO.setDate(event.getDate());
        return eventDTO;
    }


    @Async
    private Double getDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                distanceApiUrl + "&latitude1=" + latitude1 + "&longitude1=" + longitude1 +
                        "&latitude2=" + latitude2 + "&longitude2=" + longitude2, String.class);
        String result = "";
        if(response.getStatusCode() == HttpStatus.OK){
            result = extractValFromJson(response,"distance");
            if(result.equals("Information not available")){
                return -1.0;
            }
        }

        return Double.parseDouble(result);


    }

    @Async
    private String getWeather(String city, LocalDate date){
        ResponseEntity<String> response = restTemplate.getForEntity(weatherApiUrl + "&city=" + city + "&date=" + date, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return extractValFromJson(response,"weather");
        }
        return "Weather information not available";
    }

    private String extractValFromJson(ResponseEntity<String> response,String serachFor){
        String result = response.toString();
        int jsonStartIndex = result.indexOf("{");
        String jsonResponse = result.substring(jsonStartIndex);

        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonResponse);
            result = node.get(serachFor).asText();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "Information not available";
    }
}
