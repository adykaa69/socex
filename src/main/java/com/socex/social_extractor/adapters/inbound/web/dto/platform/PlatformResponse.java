package com.socex.social_extractor.adapters.inbound.web.dto.platform;

public record PlatformResponse<T>(
    String status,
    String message,
    T data
) {}
