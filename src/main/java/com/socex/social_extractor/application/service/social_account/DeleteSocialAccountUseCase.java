package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.domain.model.SocialAccount;

import java.util.UUID;

public interface DeleteSocialAccountUseCase {
    SocialAccount delete(UUID id);
}
