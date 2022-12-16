package com.example.agprocesstest.sources;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SourceServiceTest {
    public final String ASSET_NAME = "Asset1";

    @InjectMocks
    SourceService sourceService;

    @Mock
    SourceRepository sourceRepository;

    @Test
    void givenASourcePersisted_whenFindById_thenSourceIsRetrieved() {

        // given
        Source source = Source.builder()
                .name(ASSET_NAME)
                .id(1L)
                .build();
        Mockito.when(sourceRepository.findByName(ASSET_NAME))
                .thenReturn(Optional.of(source));

        // when
        var sourceOptional = sourceService.findByName(ASSET_NAME);

        // then
        Assertions.assertThat(sourceOptional).isNotNull();
        Assertions.assertThat(sourceOptional.isPresent()).isTrue();
        Assertions.assertThat(sourceOptional.get().getName()).isEqualTo(ASSET_NAME);
    }


    @Test
    void givenASourcePersisted_whenFindByName_thenSourceIsRetrieved() {

        // given
        Source source = Source.builder()
                .name(ASSET_NAME)
                .id(1L)
                .build();
        Mockito.when(sourceRepository.findByName(Mockito.any()))
                .thenReturn(Optional.of(source));

        // when
        var sourceOptional = sourceService.findByName(ASSET_NAME);

        // then
        Assertions.assertThat(sourceOptional).isNotNull();
        Assertions.assertThat(sourceOptional.isPresent()).isTrue();
        Assertions.assertThat(sourceOptional.get().getName()).isEqualTo(ASSET_NAME);
    }

    @Test
    void givenASourcePersisted_whenFindByNameStartsWithFullName_thenSourceIsRetrieved() {

        // given
        var source = Source.builder()
                .name(ASSET_NAME)
                .id(1L)
                .build();
        var pagedEvents = new PageImpl(Arrays.asList(source));
        Mockito.when(sourceRepository.findBySourceNameStartsWith(Mockito.any(), Mockito.any()))
                .thenReturn(pagedEvents);

        // when
        var page = sourceService.findBySourceNameStartsWith(ASSET_NAME, PageRequest.of(0,10));

        // then
        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.getContent()).isNotNull();
        Assertions.assertThat(page.getContent().isEmpty()).isFalse();
    }

}
