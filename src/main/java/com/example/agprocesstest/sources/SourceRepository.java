package com.example.agprocesstest.sources;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourceRepository extends CrudRepository<Source, Long> {
    Optional<Source> findByName(String name);

    @Query("select s from Source s where s.name like %:name%")
    Page<Source> findBySourceNameStartsWith(String name, Pageable pageable);
}
