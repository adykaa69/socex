package com.socex.social_extractor.domain.repository;

import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;

import java.util.List;
import java.util.UUID;

public interface SocialAccountRepository {
    List<SocialAccount> findAll();
    SocialAccount findById(UUID id);
    List<SocialAccount> findByCompanyId(UUID companyId);
    List<SocialAccount> findByPlatform(SocialAccountPlatform platform);
    List<SocialAccount> findByCompanyIdAndPlatform(UUID companyId, SocialAccountPlatform platform);
    SocialAccount save(SocialAccount socialAccount);
    SocialAccount deleteById(UUID id);
}
