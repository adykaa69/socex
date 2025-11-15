package com.socex.social_extractor.application.service.social_account.command;

import com.socex.social_extractor.domain.model.SocialAccountPlatform;

import java.util.UUID;

public record UpdateSocialAccountCommand(
        UUID id,
        SocialAccountPlatform platform,
        String accountUrl,
        String accountId
) {}
