package com.example.agprocesstest.events;

import com.example.agprocesstest.events.Event;
import com.example.agprocesstest.events.EventRepository;
import com.example.agprocesstest.events.EventService;
import com.example.agprocesstest.sources.Source;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    public final String ASSET_NAME = "Asset1";

    @InjectMocks
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    Page<Event> pagedEvents = null;

    @BeforeEach
    public void init() {
        Source source = Source.builder()
                .name(ASSET_NAME)
                .id(1L)
                .build();
        Event event = Event.builder()
                .source(source)
                .id(1L)
                .ts(Instant.now())
                .val(10L)
                .build();
        this.pagedEvents = new PageImpl(Arrays.asList(event));
    }

    @Test
    void testFindBySourceName() {

        // given
        Mockito.when(eventRepository.findBySourceName(Mockito.any(), Mockito.any()))
                .thenReturn(pagedEvents);

        // when
        Page<Event> events = eventService.findBySourceName(ASSET_NAME, PageRequest.of(0, 10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(1);
    }

    @Test
    void testFindByTs() {
        // given
        var now = Instant.ofEpochMilli(1671128022077L);
        var yesterday = now.minus(1, ChronoUnit.DAYS);
        var tomorrow = now.plus(1, ChronoUnit.DAYS);
        Mockito.when(eventRepository.findByTsIsGreaterThanAndTsIsLessThan(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(pagedEvents);

        // when
        Page<Event> events = eventService.findByTsIsGreaterThanAndTsIsLessThan(yesterday, tomorrow, PageRequest.of(0, 10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(1);
        Assertions.assertThat(events.getContent().get(0).getVal()).isEqualTo(10);
    }

    @Test
    void testFindByVal() {
        // given
        var from = 11L;
        var to = 14L;
        Mockito.when(eventRepository.findByValIsGreaterThanAndValIsLessThan(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(pagedEvents);

        // when
        Page<Event> events = eventService.findByValIsGreaterThanAndValIsLessThan(from, to, PageRequest.of(0, 10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(1);
        Assertions.assertThat(events.getContent().get(0).getVal()).isEqualTo(10);
    }

}
