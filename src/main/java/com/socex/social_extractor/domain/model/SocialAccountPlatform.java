package com.socex.social_extractor.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SocialAccountPlatform {
    FACEBOOK,
    INSTAGRAM,
    TIKTOK,
    LINKEDIN,
    YOUTUBE;

    @JsonCreator
    public static SocialAccountPlatform fromString(String platform) {
        String normalized = platform.trim().toUpperCase();
        return SocialAccountPlatform.valueOf(normalized);
    }
}
