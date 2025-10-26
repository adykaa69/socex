package com.socex.social_extractor.adapters.outbound.persistence.repository.social_account;

import com.socex.social_extractor.adapters.outbound.persistence.entity.SocialAccountEntity;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SocialAccountSpringDataRepository extends JpaRepository<SocialAccountEntity, UUID> {
    List<SocialAccountEntity> findByCompanyId(UUID companyId);
    List<SocialAccountEntity> findByPlatform(SocialAccountPlatform platform);
    SocialAccountEntity findByCompanyIdAndPlatform(UUID companyId, SocialAccountPlatform platform);
}
