package com.socex.social_extractor.adapters.inbound.web.dto.social_account;

import java.time.Instant;
import java.util.UUID;

public record SocialAccountResponse(
        UUID id,
        UUID companyId,
        String platform,
        String accountUrl,
        String accountId,
        Instant createdAt
) {}
