package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.domain.model.SocialAccount;

import java.util.UUID;

public interface GetSocialAccountByIdUseCase {
    SocialAccount getSocialAccountById(UUID id);
}
