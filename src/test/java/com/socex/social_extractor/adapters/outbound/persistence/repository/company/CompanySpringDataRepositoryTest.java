package com.socex.social_extractor.adapters.outbound.persistence.repository.company;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompanySpringDataRepositoryTest {

    @Autowired
    private CompanySpringDataRepository companyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void existsByName_ShouldReturnTrue_WhenCompanyExists() {
        // Given
        CompanyEntity entity = new CompanyEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Existing Company");
        entity.setCreatedAt(Instant.now());

        entityManager.persistAndFlush(entity);

        // When
        boolean exists = companyRepository.existsByName("Existing Company");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenCompanyDoesNotExist() {
        // When
        boolean exists = companyRepository.existsByName("NonExistent Company");

        // Then
        assertThat(exists).isFalse();
    }
}