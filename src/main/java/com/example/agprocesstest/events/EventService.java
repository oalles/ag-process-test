package com.example.agprocesstest.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Page<Event> findBySourceName(String sourceName, Pageable pageable) {
        return eventRepository.findBySourceName(sourceName, pageable);
    }

    public Page<Event> findByTsIsGreaterThanAndTsIsLessThan(Instant from, Instant to, Pageable pageable) {
        return eventRepository.findByTsIsGreaterThanAndTsIsLessThan(from, to, pageable);
    }

    public Page<Event> findByValIsGreaterThanAndValIsLessThan(Long from, Long to, Pageable pageable) {
        return eventRepository.findByValIsGreaterThanAndValIsLessThan(from, to, pageable);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Page<Event> findBySourceNameStartsWith(String name, Pageable pageable) {
        return eventRepository.findBySourceNameStartsWith(name, pageable);
    }
}
