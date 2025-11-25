package com.socex.social_extractor.adapters.inbound.web.dto.error;

public record ValidationError(
        String field,
        String message
) {}
