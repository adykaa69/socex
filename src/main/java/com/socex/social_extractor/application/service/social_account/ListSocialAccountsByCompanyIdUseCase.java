package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.domain.model.SocialAccount;

import java.util.List;
import java.util.UUID;

public interface ListSocialAccountsByCompanyIdUseCase {
    List<SocialAccount> getSocialAccountsByCompanyId(UUID companyId);
}
