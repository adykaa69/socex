package com.socex.social_extractor.adapters.inbound.web.dto.social_account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SocialAccountRequest(
        @NotNull(message = "Company ID is required")
        UUID companyId,
        @NotBlank(message = "Platform is required")
        String platform,
        @NotBlank(message = "Account URL is required")
        String accountUrl,
        String accountId
) {}
