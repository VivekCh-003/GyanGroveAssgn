package com.example.ggAssgn.repository;

import com.example.ggAssgn.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
