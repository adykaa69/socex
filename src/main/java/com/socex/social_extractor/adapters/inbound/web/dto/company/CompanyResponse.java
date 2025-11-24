package com.socex.social_extractor.adapters.inbound.web.dto.company;

import java.time.Instant;
import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {}
