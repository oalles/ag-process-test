package com.example.agprocesstest.sources;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DataJpaTest
@DirtiesContext
public class SourceRepositoryTest {

    public final String ASSET_NAME = "Asset1";
    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void givenASource_whenPersisted_thenSuccess() {

        // given
        var source = new Source();
        source.setName(ASSET_NAME);

        // when
        source = testEntityManager.persist(source);

        // then
        Assertions.assertThat(source).isNotNull();
        Assertions.assertThat(source.getName()).isEqualTo(ASSET_NAME);
        Assertions.assertThat(source.getId()).isNotNull();
    }

    @Test
    void givenTestData_whenFindId_thenSourceIsRetrieved() {

        // when
        Optional<Source> sourceOptional = sourceRepository.findById(1L);

        // then
        Assertions.assertThat(sourceOptional).isNotNull();
        Assertions.assertThat(sourceOptional.isPresent()).isTrue();
        Assertions.assertThat(sourceOptional.get().getName()).isEqualTo(ASSET_NAME);
    }

    @Test
    void givenTestData_whenFindByExactName_thenSourceRetrieved() {

        // when
        Optional<Source> sourceOptional = sourceRepository.findByName(ASSET_NAME);

        // then
        Assertions.assertThat(sourceOptional).isNotNull();
        Assertions.assertThat(sourceOptional.isPresent()).isTrue();
        Assertions.assertThat(sourceOptional.get().getName()).isEqualTo(ASSET_NAME);
    }

    @Test
    void givenTestData_whenFindByNameStartsWith_thenSourceRetrieved() {

        // when
        var page = sourceRepository.findBySourceNameStartsWith(ASSET_NAME.substring(0,3), PageRequest.of(0,10));

        // then
        Assertions.assertThat(page).isNotNull();

        Assertions.assertThat(page.getContent()).isNotNull();
        Assertions.assertThat(page.getContent().isEmpty()).isFalse();
    }
}
