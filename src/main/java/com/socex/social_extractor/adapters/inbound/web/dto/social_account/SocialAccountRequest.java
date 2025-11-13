package com.socex.social_extractor.adapters.inbound.web.dto.social_account;

import java.util.UUID;

public record SocialAccountRequest(
        UUID companyId,
        String platform,
        String accountUrl,
        String accountId
) {}
