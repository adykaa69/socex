package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.domain.model.SocialAccount;

public interface CreateSocialAccountUseCase {
    SocialAccount create(SocialAccount socialAccount);
}
