package com.socex.social_extractor.domain.factory;

import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyFactoryTest {

    @Test
    void create_ShouldReturnNewCompany_FromCommand() {
        // Given
        CreateCompanyCommand command = new CreateCompanyCommand("Test Company");

        // When
        Company company = CompanyFactory.create(command);

        // Then
        assertThat(company).isNotNull();
        assertThat(company.id()).isNotNull();
        assertThat(company.name()).isEqualTo("Test Company");
        assertThat(company.createdAt()).isNull();
        assertThat(company.updatedAt()).isNull();
    }

    @Test
    void update_ShouldReturnUpdatedCompany_BasedOnCommand() {
        // Given
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Company existingCompany = new Company(id, "Old Name", createdAt, null);

        UpdateCompanyCommand command = new UpdateCompanyCommand(id, "New Name");

        // When
        Company updatedCompany = CompanyFactory.update(existingCompany, command);

        // Then
        assertThat(updatedCompany.id()).isEqualTo(id);
        assertThat(updatedCompany.name()).isEqualTo("New Name");
        assertThat(updatedCompany.createdAt()).isEqualTo(createdAt);
        assertThat(updatedCompany.updatedAt()).isNull();
    }

}