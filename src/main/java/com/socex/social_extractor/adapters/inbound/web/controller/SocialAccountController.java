package com.socex.social_extractor.adapters.inbound.web.controller;


import com.socex.social_extractor.adapters.inbound.web.dto.platform.PlatformResponse;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountResponse;
import com.socex.social_extractor.adapters.inbound.web.mapper.SocialAccountWebMapper;
import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import jakarta.validation.Valid;
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

    private final SocialAccountUseCaseFacade socialAccountUseCaseFacade;
    private final SocialAccountWebMapper socialAccountWebMapper;

    public SocialAccountController(SocialAccountUseCaseFacade socialAccountUseCaseFacade,
                                   SocialAccountWebMapper socialAccountWebMapper) {
        this.socialAccountUseCaseFacade = socialAccountUseCaseFacade;
        this.socialAccountWebMapper = socialAccountWebMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> getSocialAccount(@PathVariable UUID id) {
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
        List<SocialAccount> socialAccounts;

        if (companyId != null && platform != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByCompanyIdAndPlatform(companyId, platform);
        } else if (companyId != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByCompanyId(companyId);
        } else if (platform != null) {
            socialAccounts = socialAccountUseCaseFacade.getSocialAccountsByPlatform(platform);
        } else {
            socialAccounts = socialAccountUseCaseFacade.getAllSocialAccounts();
        }

        List<SocialAccountResponse> socialAccountResponses = socialAccounts.stream()
                .map(socialAccountWebMapper::socialAccountToSocialAccountResponse)
                .toList();

        return new PlatformResponse<>("success", "Social accounts retrieved successfully", socialAccountResponses);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<SocialAccountResponse> createSocialAccount(@Valid @RequestBody SocialAccountRequest socialAccountRequest) {
        CreateSocialAccountCommand command =
                socialAccountWebMapper.socialAccountRequestToCreateSocialAccountCommand(socialAccountRequest);
        SocialAccount savedSocialAccount = socialAccountUseCaseFacade.create(command);
        SocialAccountResponse socialAccountResponse =
                socialAccountWebMapper.socialAccountToSocialAccountResponse(savedSocialAccount);
        return new PlatformResponse<>("success", "Social account created successfully", socialAccountResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> deleteSocialAccount(@PathVariable UUID id) {
        SocialAccount deletedSocialAccount = socialAccountUseCaseFacade.delete(id);
        SocialAccountResponse socialAccountResponse = socialAccountWebMapper.socialAccountToSocialAccountResponse(deletedSocialAccount);
        return new PlatformResponse<>("success", "Social account deleted successfully", socialAccountResponse);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<SocialAccountResponse> updateSocialAccount(
            @PathVariable UUID id,
            @Valid @RequestBody SocialAccountRequest socialAccountRequest
    ) {
        UpdateSocialAccountCommand command =
                socialAccountWebMapper.socialAccountRequestToUpdateSocialAccountCommand(id, socialAccountRequest);
        SocialAccount updatedSocialAccount = socialAccountUseCaseFacade.update(command);
        SocialAccountResponse socialAccountResponse =
                socialAccountWebMapper.socialAccountToSocialAccountResponse(updatedSocialAccount);
        return new PlatformResponse<>("success", "Social account updated successfully", socialAccountResponse);
    }
}
