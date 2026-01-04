package com.socex.social_extractor.adapters.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyResponse;
import com.socex.social_extractor.adapters.inbound.web.mapper.CompanyWebMapper;
import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.application.service.orchestrator.ServiceOrchestrator;
import com.socex.social_extractor.domain.exception.CompanyAlreadyExistsException;
import com.socex.social_extractor.domain.exception.CompanyNotFoundException;
import com.socex.social_extractor.domain.model.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyUseCaseFacade companyUseCaseFacade;

    @MockitoBean
    private ServiceOrchestrator serviceOrchestrator;

    @MockitoBean
    private CompanyWebMapper companyWebMapper;

    private final UUID COMPANY_ID = UUID.randomUUID();
    private final String COMPANY_NAME = "Test Company";

    @Nested
    @DisplayName("GET /api/v1/companies/{id}")
    class GetCompanyTests {

        @Test
        void shouldReturnCompanyResponseAndStatusOk_WhenFound() throws Exception {
            // Given
            Company company = new Company(COMPANY_ID, COMPANY_NAME, Instant.now(), null);
            CompanyResponse response = new CompanyResponse(COMPANY_ID, COMPANY_NAME, Instant.now(), null);

            given(companyUseCaseFacade.getCompany(COMPANY_ID)).willReturn(company);
            given(companyWebMapper.companyToCompanyResponse(company)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/companies/{id}", COMPANY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Company retrieved successfully"))
                    .andExpect(jsonPath("$.data.id").value(COMPANY_ID.toString()))
                    .andExpect(jsonPath("$.data.name").value(COMPANY_NAME));
        }

        @Test
        void shouldReturnStatusNotFound_WhenNotFound() throws Exception {
            // Given
            given(companyUseCaseFacade.getCompany(COMPANY_ID))
                    .willThrow(new CompanyNotFoundException(COMPANY_ID));

            // When/Then
            mockMvc.perform(get("/api/v1/companies/{id}", COMPANY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Company not found with id: " + COMPANY_ID))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/companies")
    class GetAllCompaniesTests {

        @Test
        void shouldReturnAllCompanyResponsesAndStatusOk_WhenFound() throws Exception {
            // Given
            final UUID companyId_2 = UUID.randomUUID();
            final String companyName_2 = "Another Test Company";

            Company company_1 = new Company(COMPANY_ID, COMPANY_NAME, Instant.now(), null);
            Company company_2 = new Company(companyId_2, companyName_2, Instant.now(), null);
            CompanyResponse companyResponse_1 = new CompanyResponse(COMPANY_ID, COMPANY_NAME, Instant.now(), null);
            CompanyResponse companyResponse_2 = new CompanyResponse(companyId_2, companyName_2, Instant.now(), null);

            given(companyUseCaseFacade.getAllCompanies()).willReturn(List.of(company_1, company_2));
            given(companyWebMapper.companyToCompanyResponse(company_1)).willReturn(companyResponse_1);
            given(companyWebMapper.companyToCompanyResponse(company_2)).willReturn(companyResponse_2);

            // When/Then
            mockMvc.perform(get("/api/v1/companies")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("All companies retrieved successfully"))
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].id").value(COMPANY_ID.toString()))
                    .andExpect(jsonPath("$.data[1].id").value(companyId_2.toString()));
        }

        @Test
        void shouldReturnEmptyListStatusOk_WhenNoCompaniesExist() throws Exception {
            // Given
            given(companyUseCaseFacade.getAllCompanies()).willReturn(List.of());

            // When/Then
            mockMvc.perform(get("/api/v1/companies"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("All companies retrieved successfully"))
                    .andExpect(jsonPath("$.data").isEmpty());

        }
    }

        @Nested
        @DisplayName("POST /api/v1/companies")
        class CreateCompanyTests {

            @Test
            void shouldReturnCompanyResponseAndStatusOk_WhenValid() throws Exception {
                // Given
                CompanyRequest request = new CompanyRequest(COMPANY_NAME);
                CreateCompanyCommand command = new CreateCompanyCommand(COMPANY_NAME);
                Company company = new Company(COMPANY_ID, COMPANY_NAME, Instant.now(), null);
                CompanyResponse response = new CompanyResponse(COMPANY_ID, COMPANY_NAME, Instant.now(), null);

                given(companyWebMapper.companyRequestToCreateCompanyCommand(any())).willReturn(command);
                given(companyUseCaseFacade.create(command)).willReturn(company);
                given(companyWebMapper.companyToCompanyResponse(company)).willReturn(response);

                // When/Then
                mockMvc.perform(post("/api/v1/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.status").value("success"))
                        .andExpect(jsonPath("$.message").value("Company created successfully"))
                        .andExpect(jsonPath("$.data.id").value(COMPANY_ID.toString()));
            }

            @Test
            void shouldReturnStatusBadRequest_WhenNameIsBlank() throws Exception {
                // Given
                CompanyRequest request = new CompanyRequest("");

                // When/Then
                mockMvc.perform(post("/api/v1/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value("error"))
                        .andExpect(jsonPath("$.message").value("Input validation failed"))
                        .andExpect(jsonPath("$.data.errorCode").value("INPUT_VALIDATION_ERROR"))
                        .andExpect(jsonPath("$.data.validationErrors[0].field").value("name"));
            }

            @Test
            void shouldReturnStatusConflict_WhenNameAlreadyExists() throws Exception {
                // Given
                CompanyRequest request = new CompanyRequest(COMPANY_NAME);
                CreateCompanyCommand command = new CreateCompanyCommand(COMPANY_NAME);

                given(companyWebMapper.companyRequestToCreateCompanyCommand(any())).willReturn(command);
                given(companyUseCaseFacade.create(command))
                        .willThrow(new CompanyAlreadyExistsException(COMPANY_NAME));

                // When/Then
                mockMvc.perform(post("/api/v1/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.status").value("error"))
                        .andExpect(jsonPath("$.message").value("Company already exists with name: " + COMPANY_NAME))
                        .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_ALREADY_EXISTS"));
            }
        }

    @Nested
    @DisplayName("PUT /api/v1/companies/{id}")
    class UpdateCompany {

        @Test
        void shouldReturnStatusOk_WhenValid() throws Exception {
            // Given
            CompanyRequest request = new CompanyRequest(COMPANY_NAME);
            UpdateCompanyCommand command = new UpdateCompanyCommand(COMPANY_ID, COMPANY_NAME);
            Company updatedCompany = new Company(COMPANY_ID, "New Name", Instant.now(), Instant.now());
            CompanyResponse response = new CompanyResponse(COMPANY_ID, "New Name", Instant.now(), Instant.now());

            given(companyWebMapper.companyRequestToUpdateCompanyCommand(eq(COMPANY_ID), any())).willReturn(command);
            given(companyUseCaseFacade.update(command)).willReturn(updatedCompany);
            given(companyWebMapper.companyToCompanyResponse(updatedCompany)).willReturn(response);

            // When/Then
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Company updated successfully"))
                    .andExpect(jsonPath("$.data.name").value("New Name"))
                    .andExpect(jsonPath("$.data.id").value(COMPANY_ID.toString()));
        }

        @Test
        void shouldReturnStatusBadRequest_WhenNameIsBlank() throws Exception {
            // Given
            CompanyRequest request = new CompanyRequest("");

            // When/Then
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Input validation failed"))
                    .andExpect(jsonPath("$.data.errorCode").value("INPUT_VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.data.validationErrors[0].field").value("name"));
        }

        @Test
        @DisplayName("Should return 409 Conflict when new name already exists")
        void shouldReturnStatusConflict_WhenNameAlreadyExists() throws Exception {
            // Given
            String duplicateName = COMPANY_NAME;
            CompanyRequest request = new CompanyRequest(duplicateName);
            UpdateCompanyCommand command = new UpdateCompanyCommand(COMPANY_ID, duplicateName);

            given(companyWebMapper.companyRequestToUpdateCompanyCommand(eq(COMPANY_ID), any())).willReturn(command);
            given(companyUseCaseFacade.update(command))
                    .willThrow(new CompanyAlreadyExistsException(duplicateName));

            // When/Then
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Company already exists with name: " + duplicateName))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_ALREADY_EXISTS"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when ID does not exist")
        void shouldReturnStatusNotFound_WhenNotFound() throws Exception {
            // Given
            CompanyRequest request = new CompanyRequest(COMPANY_NAME);
            UpdateCompanyCommand command = new UpdateCompanyCommand(COMPANY_ID, COMPANY_NAME);

            given(companyWebMapper.companyRequestToUpdateCompanyCommand(eq(COMPANY_ID), any())).willReturn(command);
            given(companyUseCaseFacade.update(command))
                    .willThrow(new CompanyNotFoundException(COMPANY_ID));

            // When/Then
            mockMvc.perform(put("/api/v1/companies/{id}", COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Company not found with id: " + COMPANY_ID))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/companies/{id}")
    class DeleteCompany {

        @Test
        void shouldReturnStatusOk_AndCallOrchestrator_WhenFound() throws Exception {
            // Given
            Company deletedCompany = new Company(COMPANY_ID, COMPANY_NAME, Instant.now(), null);
            CompanyResponse response = new CompanyResponse(COMPANY_ID, COMPANY_NAME, Instant.now(), null);

            given(serviceOrchestrator.deleteCompany(COMPANY_ID)).willReturn(deletedCompany);
            given(companyWebMapper.companyToCompanyResponse(deletedCompany)).willReturn(response);

            // When/Then
            mockMvc.perform(delete("/api/v1/companies/{id}", COMPANY_ID)
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Company deleted successfully"))
                    .andExpect(jsonPath("$.data.id").value(COMPANY_ID.toString()));

            verify(serviceOrchestrator).deleteCompany(COMPANY_ID);
        }

        @Test
        void shouldReturnStatusNotFound_WhenNotFound() throws Exception {
            // Given
            // The Controller calls ServiceOrchestrator, which calls CompanyService, which throws the exception
            given(serviceOrchestrator.deleteCompany(COMPANY_ID))
                    .willThrow(new CompanyNotFoundException(COMPANY_ID));

            // When/Then
            mockMvc.perform(delete("/api/v1/companies/{id}", COMPANY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Company not found with id: " + COMPANY_ID))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }
}


