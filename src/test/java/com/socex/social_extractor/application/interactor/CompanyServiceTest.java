package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.exception.CompanyAlreadyExistsException;
import com.socex.social_extractor.domain.exception.CompanyNotFoundException;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService underTest;

    @Test
    void create_ShouldThrowCompanyAlreadyExistsException_WhenCompanyAlreadyExists() {
        // Given
        CreateCompanyCommand command  = new CreateCompanyCommand("Existing Company");
        given(companyRepository.existsByName("Existing Company")).willReturn(true);

        // When / Then
        assertThatThrownBy(() -> underTest.create(command))
                .isInstanceOf(CompanyAlreadyExistsException.class)
                .hasMessage("Company already exists with name: %s", command.name());

        verify(companyRepository, never()).save(any());
    }

    @Test
    void create_ShouldSaveAndReturnCompany_WhenNameIsUnique() {
        // Given
        CreateCompanyCommand command = new CreateCompanyCommand("New Company");
        given(companyRepository.existsByName("New Company")).willReturn(false);
        given(companyRepository.save(any(Company.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        Company result = underTest.create(command);

        // Then
        assertThat(result.name()).isEqualTo("New Company");
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void getCompany_ShouldThrowCompanyNotFoundException_WhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        given(companyRepository.findById(id)).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> underTest.getCompany(id))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessage("Company not found with id: %s", id);
    }

    @Test
    void update_ShouldThrowCompanyAlreadyExistsException_WhenRenamingToExistingName() {
        // Given
        UUID id = UUID.randomUUID();
        Company existingCompany = new Company(id, "Old Name", Instant.now(), null);
        UpdateCompanyCommand command = new UpdateCompanyCommand(id, "Taken Name");

        given(companyRepository.findById(id)).willReturn(Optional.of(existingCompany));
        given(companyRepository.existsByName("Taken Name")).willReturn(true);

        // When/Then
        assertThatThrownBy(() -> underTest.update(command))
                .isInstanceOf(CompanyAlreadyExistsException.class)
                .hasMessage("Company already exists with name: %s", command.name());
    }
}