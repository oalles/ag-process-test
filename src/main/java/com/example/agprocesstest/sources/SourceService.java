package com.example.agprocesstest.sources;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

    public Optional<Source> findByName(String name) {
        return sourceRepository.findByName(name);
    }

    public Page<Source> findBySourceNameStartsWith(String name, Pageable pageable) {
        return sourceRepository.findBySourceNameStartsWith(name, pageable);
    }

    public Optional<Source> findById(Long id) {
        return sourceRepository.findById(id);
    }

    public void delete(Source source) {
        sourceRepository.delete(source);
    }

    public Source save(Source source) {
        return sourceRepository.save(source);
    }
}
