package com.example.agprocesstest.events;

import com.example.agprocesstest.sources.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findBySourceName(String sourceName, Pageable pageable);

    @Query("select e from Event e where e.source.name like %:name%")
    Page<Event> findBySourceNameStartsWith(String name, Pageable pageable);

    Page<Event> findByTsIsGreaterThanAndTsIsLessThan(Instant from, Instant to, Pageable pageable);

    Page<Event> findByValIsGreaterThanAndValIsLessThan(Long from, Long to, Pageable pageable);


}
