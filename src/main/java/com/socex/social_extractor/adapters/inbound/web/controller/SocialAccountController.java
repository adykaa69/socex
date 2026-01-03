package com.socex.social_extractor.adapters.inbound.web.controller;


import com.socex.social_extractor.adapters.inbound.web.dto.platform.PlatformResponse;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountResponse;
import com.socex.social_extractor.adapters.inbound.web.mapper.SocialAccountWebMapper;
import com.socex.social_extractor.application.service.orchestrator.ServiceOrchestrator;
import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/social-accounts")
public class SocialAccountController {

    private static final Logger log = LoggerFactory.getLogger(SocialAccountController.class);

    private final SocialAccountUseCaseFacade socialAccountUseCaseFacade;
    private final ServiceOrchestrator serviceOrchestrator;
    private final SocialAccountWebMapper socialAccountWebMapper;

    public SocialAccountController(SocialAccountUseCaseFacade socialAccountUseCaseFacade,
                                   ServiceOrchestrator serviceOrchestrator,
                                   SocialAccountWebMapper socialAccountWebMapper) {
        this.socialAccountUseCaseFacade = socialAccountUseCaseFacade;
        this.serviceOrchestrator = serviceOrchestrator;
        this.socialAccountWebMapper = socialAccountWebMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> getSocialAccount(@PathVariable UUID id) {
        log.info("Fetching social account with ID: {}", id);
        SocialAccount socialAccount = socialAccountUseCaseFacade.getSocialAccountById(id);
        SocialAccountResponse socialAccountResponse = socialAccountWebMapper.socialAccountToSocialAccountResponse(socialAccount);
        return new PlatformResponse<>("success", "Social account retrieved successfully", socialAccountResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<SocialAccountResponse>> getSocialAccountsBasedOnParameters(
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) SocialAccountPlatform platform
    ) {
        log.info("Fetching social accounts - companyId: {}, platform: {}", companyId, platform);
        List<SocialAccount> socialAccounts;

        if (companyId != null && platform != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByCompanyIdAndPlatform(companyId, platform);
        } else if (companyId != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByCompanyId(companyId);
        } else if (platform != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByPlatform(platform);
        } else {
            log.debug("No parameters provided, fetching all social accounts");
            socialAccounts = socialAccountUseCaseFacade.getAllSocialAccounts();
        }

        List<SocialAccountResponse> socialAccountResponses = socialAccounts.stream()
                .map(socialAccountWebMapper::socialAccountToSocialAccountResponse)
                .toList();

        log.info("Fetched {} social accounts based on parameters", socialAccountResponses.size());

        return new PlatformResponse<>("success", "Social accounts retrieved successfully", socialAccountResponses);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<SocialAccountResponse> createSocialAccount(@Valid @RequestBody SocialAccountRequest socialAccountRequest) {
        log.info("Creating new social account for companyId: {} on platform: {}",
                socialAccountRequest.companyId(), socialAccountRequest.platform());

        CreateSocialAccountCommand command =
                socialAccountWebMapper.socialAccountRequestToCreateSocialAccountCommand(socialAccountRequest);
        SocialAccount savedSocialAccount = serviceOrchestrator.createSocialAccount(command);
        SocialAccountResponse socialAccountResponse =
                socialAccountWebMapper.socialAccountToSocialAccountResponse(savedSocialAccount);

        log.info("Social Account created with ID: {}", savedSocialAccount.id());
        return new PlatformResponse<>("success", "Social account created successfully", socialAccountResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> deleteSocialAccount(@PathVariable UUID id) {
        log.info("Deleting social account with ID: {}", id);

        SocialAccount deletedSocialAccount = socialAccountUseCaseFacade.delete(id);
        SocialAccountResponse socialAccountResponse = socialAccountWebMapper.socialAccountToSocialAccountResponse(deletedSocialAccount);
        log.info("Social account deleted with ID: {}", id);

        return new PlatformResponse<>("success", "Social account deleted successfully", socialAccountResponse);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> updateSocialAccount(
            @PathVariable UUID id,
            @Valid @RequestBody SocialAccountRequest socialAccountRequest
    ) {
        log.info("Updating social account with ID: {}", id);

        UpdateSocialAccountCommand command =
                socialAccountWebMapper.socialAccountRequestToUpdateSocialAccountCommand(id, socialAccountRequest);
        SocialAccount updatedSocialAccount = socialAccountUseCaseFacade.update(command);
        SocialAccountResponse socialAccountResponse =
                socialAccountWebMapper.socialAccountToSocialAccountResponse(updatedSocialAccount);

        log.info("Social account updated with ID: {}", id);
        return new PlatformResponse<>("success", "Social account updated successfully", socialAccountResponse);
    }
}
