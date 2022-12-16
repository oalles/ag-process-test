package com.example.agprocesstest.events;

import com.example.agprocesstest.events.Event;
import com.example.agprocesstest.events.EventRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@DataJpaTest
@DirtiesContext
public class EventRepositoryTest {

    public final String ASSET_NAME = "Asset1";
    @Autowired
    private EventRepository eventRepository;

    @Test
    void givenTestData_whenFindByExactSourceName_then5Sources() {

        // when
        var events = eventRepository.findBySourceName(ASSET_NAME, PageRequest.of(0,10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(5);
    }

    @Test
    void givenTestData_whenFindBySourceNameStartsWith_then10Sources() {

        // given
        var prefix = ASSET_NAME.substring(0,4);

        // when
        var events = eventRepository.findBySourceNameStartsWith(prefix, PageRequest.of(0,10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(10);
    }

    @Test
    void givenTestData_whenFindByTsInRange_then() {
        // given
        var now = Instant.ofEpochMilli(1671128022077L);
        var yesterday = now.minus(1, ChronoUnit.DAYS);
        var tomorrow = now.plus(1, ChronoUnit.DAYS);

        // when
        Page<Event> events = eventRepository.findByTsIsGreaterThanAndTsIsLessThan(yesterday, tomorrow, PageRequest.of(0,10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(2);
        Assertions.assertThat(events.getContent().get(0).getVal()).isEqualTo(10);
    }

    @Test
    void givenTestData_whenFindByValInRange_then() {
        // given
        var from = 11L;
        var to = 14L;

        // when
        Page<Event> events = eventRepository.findByValIsGreaterThanAndValIsLessThan(from, to, PageRequest.of(0,10));

        // then
        Assertions.assertThat(events).isNotNull();
        Assertions.assertThat(events.getContent()).isNotNull();
        Assertions.assertThat(events.getContent().size()).isEqualTo(8);
        Assertions.assertThat(events.getContent().get(0).getVal()).isEqualTo(12);
    }

}
