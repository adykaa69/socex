package com.socex.social_extractor.adapters.inbound.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountResponse;
import com.socex.social_extractor.adapters.inbound.web.mapper.SocialAccountWebMapper;
import com.socex.social_extractor.application.service.orchestrator.ServiceOrchestrator;
import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.exception.SocialAccountNotFoundException;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
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

@WebMvcTest(SocialAccountController.class)
class SocialAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SocialAccountUseCaseFacade socialAccountUseCaseFacade;

    @MockitoBean
    private ServiceOrchestrator serviceOrchestrator;

    @MockitoBean
    private SocialAccountWebMapper socialAccountWebMapper;

    private final UUID ACCOUNT_ID = UUID.randomUUID();
    private final UUID COMPANY_ID = UUID.randomUUID();
    private final String ACCOUNT_URL = "https://facebook.com/test";
    private final String PLATFORM_STR = "FACEBOOK";

    @Nested
    @DisplayName("GET /api/v1/social-accounts/{id}")
    class GetSocialAccountTests {

        @Test
        void shouldReturnSocialAccountResponseAndStatusOk_WhenFound() throws Exception {
            // Given
            SocialAccount account = new SocialAccount(ACCOUNT_ID, COMPANY_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "id", Instant.now(), null);
            SocialAccountResponse response = new SocialAccountResponse(ACCOUNT_ID, COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "id", Instant.now(), null);

            given(socialAccountUseCaseFacade.getSocialAccountById(ACCOUNT_ID)).willReturn(account);
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Social account retrieved successfully"))
                    .andExpect(jsonPath("$.data.id").value(ACCOUNT_ID.toString()))
                    .andExpect(jsonPath("$.data.platform").value(PLATFORM_STR));
        }

        @Test
        void shouldReturnStatusNotFound_WhenNotFound() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.getSocialAccountById(ACCOUNT_ID))
                    .willThrow(new SocialAccountNotFoundException(ACCOUNT_ID));

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("SocialAccount not found with id: " + ACCOUNT_ID))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/social-accounts")
    class GetSocialAccountsTests {

        private final SocialAccount account = new SocialAccount(ACCOUNT_ID, COMPANY_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "id", Instant.now(), null);
        private final SocialAccountResponse response = new SocialAccountResponse(ACCOUNT_ID, COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "id", Instant.now(), null);

        @Test
        void shouldReturnAllAccounts_WhenNoParamsProvided() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.getAllSocialAccounts()).willReturn(List.of(account));
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @Test
        void shouldFilterByCompanyId_WhenCompanyIdProvided() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.getSocialAccountsByCompanyId(COMPANY_ID)).willReturn(List.of(account));
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts")
                            .param("companyId", COMPANY_ID.toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data[0].companyId").value(COMPANY_ID.toString()));
        }

        @Test
        void shouldFilterByPlatform_WhenPlatformProvided() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.getSocialAccountsByPlatform(SocialAccountPlatform.FACEBOOK)).willReturn(List.of(account));
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts")
                            .param("platform", "FACEBOOK")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data[0].platform").value("FACEBOOK"));
        }

        @Test
        void shouldFilterByCompanyIdAndPlatform_WhenBothProvided() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.getSocialAccountsByCompanyIdAndPlatform(COMPANY_ID, SocialAccountPlatform.FACEBOOK))
                    .willReturn(List.of(account));
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(get("/api/v1/social-accounts")
                            .param("companyId", COMPANY_ID.toString())
                            .param("platform", "FACEBOOK")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data[0].companyId").value(COMPANY_ID.toString()))
                    .andExpect(jsonPath("$.data[0].platform").value("FACEBOOK"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/social-accounts")
    class CreateSocialAccountTests {

        @Test
        void shouldReturnStatusCreated_AndCallOrchestrator_WhenValid() throws Exception {
            // Given
            SocialAccountRequest request = new SocialAccountRequest(COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "accId");
            CreateSocialAccountCommand command = new CreateSocialAccountCommand(COMPANY_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "accId");
            SocialAccount account = new SocialAccount(ACCOUNT_ID, COMPANY_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "accId", Instant.now(), null);
            SocialAccountResponse response = new SocialAccountResponse(ACCOUNT_ID, COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "accId", Instant.now(), null);

            given(socialAccountWebMapper.socialAccountRequestToCreateSocialAccountCommand(any())).willReturn(command);
            given(serviceOrchestrator.createSocialAccount(command)).willReturn(account);
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(account)).willReturn(response);

            // When/Then
            mockMvc.perform(post("/api/v1/social-accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Social account created successfully"))
                    .andExpect(jsonPath("$.data.id").value(ACCOUNT_ID.toString()));

            verify(serviceOrchestrator).createSocialAccount(command);
        }

        @Test
        void shouldReturnStatusBadRequest_WhenCompanyIdIsNull() throws Exception {
            SocialAccountRequest request = new SocialAccountRequest(null, PLATFORM_STR, ACCOUNT_URL, "accId");

            // When/Then
            mockMvc.perform(post("/api/v1/social-accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data.errorCode").value("INPUT_VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.data.validationErrors[0].field").value("companyId"));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/social-accounts/{id}")
    class UpdateSocialAccountTests {

        @Test
        void shouldReturnStatusOk_WhenValid() throws Exception {
            // Given
            SocialAccountRequest request = new SocialAccountRequest(COMPANY_ID, "YOUTUBE", "newUrl", "newId");
            UpdateSocialAccountCommand command = new UpdateSocialAccountCommand(ACCOUNT_ID, SocialAccountPlatform.YOUTUBE, "newUrl", "newId");
            SocialAccount updatedAccount = new SocialAccount(ACCOUNT_ID, COMPANY_ID, SocialAccountPlatform.YOUTUBE, "newUrl", "newId", Instant.now(), Instant.now());
            SocialAccountResponse response = new SocialAccountResponse(ACCOUNT_ID, COMPANY_ID, "YOUTUBE", "newUrl", "newId", Instant.now(), Instant.now());

            given(socialAccountWebMapper.socialAccountRequestToUpdateSocialAccountCommand(eq(ACCOUNT_ID), any())).willReturn(command);
            given(socialAccountUseCaseFacade.update(command)).willReturn(updatedAccount);
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(updatedAccount)).willReturn(response);

            // When/Then
            mockMvc.perform(put("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Social account updated successfully"))
                    .andExpect(jsonPath("$.data.platform").value("YOUTUBE"));
        }

        @Test
        void shouldReturnStatusNotFound_WhenIdNotFound() throws Exception {
            // Given
            SocialAccountRequest request = new SocialAccountRequest(COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "id");
            UpdateSocialAccountCommand command = new UpdateSocialAccountCommand(ACCOUNT_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "id");

            given(socialAccountWebMapper.socialAccountRequestToUpdateSocialAccountCommand(eq(ACCOUNT_ID), any())).willReturn(command);
            given(socialAccountUseCaseFacade.update(command))
                    .willThrow(new SocialAccountNotFoundException(ACCOUNT_ID));

            // When/Then
            mockMvc.perform(put("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/social-accounts/{id}")
    class DeleteSocialAccountTests {

        @Test
        void shouldReturnStatusOk_WhenFound() throws Exception {
            // Given
            SocialAccount deletedAccount = new SocialAccount(ACCOUNT_ID, COMPANY_ID, SocialAccountPlatform.FACEBOOK, ACCOUNT_URL, "id", Instant.now(), null);
            SocialAccountResponse response = new SocialAccountResponse(ACCOUNT_ID, COMPANY_ID, PLATFORM_STR, ACCOUNT_URL, "id", Instant.now(), null);

            given(socialAccountUseCaseFacade.delete(ACCOUNT_ID)).willReturn(deletedAccount);
            given(socialAccountWebMapper.socialAccountToSocialAccountResponse(deletedAccount)).willReturn(response);

            // When/Then
            mockMvc.perform(delete("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value("Social account deleted successfully"))
                    .andExpect(jsonPath("$.data.id").value(ACCOUNT_ID.toString()));
        }

        @Test
        void shouldReturnStatusNotFound_WhenNotFound() throws Exception {
            // Given
            given(socialAccountUseCaseFacade.delete(ACCOUNT_ID))
                    .willThrow(new SocialAccountNotFoundException(ACCOUNT_ID));

            // When/Then
            mockMvc.perform(delete("/api/v1/social-accounts/{id}", ACCOUNT_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("SocialAccount not found with id: " + ACCOUNT_ID))
                    .andExpect(jsonPath("$.data.errorCode").value("RESOURCE_NOT_FOUND"));
        }
    }
}