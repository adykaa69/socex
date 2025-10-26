package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;

import java.util.List;

public interface ListSocialAccountsByPlatformUseCase {
    List<SocialAccount> getSocialAccountsByPlatform(SocialAccountPlatform platform);
}
