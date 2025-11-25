package com.socex.social_extractor.domain.exception;

import java.util.UUID;

public class SocialAccountNotFoundException extends ResourceNotFoundException {
    public SocialAccountNotFoundException(UUID id) {
        super("SocialAccount", id);
    }
}
