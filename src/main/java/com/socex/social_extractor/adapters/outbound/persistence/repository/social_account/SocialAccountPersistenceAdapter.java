package com.socex.social_extractor.adapters.outbound.persistence.repository.social_account;

import com.socex.social_extractor.adapters.outbound.persistence.entity.SocialAccountEntity;
import com.socex.social_extractor.adapters.outbound.persistence.mapper.SocialAccountPersistenceMapper;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import com.socex.social_extractor.domain.repository.SocialAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SocialAccountPersistenceAdapter implements SocialAccountRepository {

    private final SocialAccountSpringDataRepository socialAccountRepository;
    private final SocialAccountPersistenceMapper socialAccountPersistenceMapper;

    public SocialAccountPersistenceAdapter(SocialAccountSpringDataRepository socialAccountRepository,
                                           SocialAccountPersistenceMapper socialAccountPersistenceMapper) {
        this.socialAccountRepository = socialAccountRepository;
        this.socialAccountPersistenceMapper = socialAccountPersistenceMapper;
    }

    @Override
    public SocialAccount save(SocialAccount socialAccount) {
        SocialAccountEntity socialAccountEntity = socialAccountPersistenceMapper.socialAccountToSocialAccountEntity(socialAccount);
        SocialAccountEntity savedEntity = socialAccountRepository.save(socialAccountEntity);
        return socialAccountPersistenceMapper.socialAccountEntityToSocialAccount(savedEntity);
    }

    @Override
    public SocialAccount findById(UUID id) {
        SocialAccountEntity socialAccountEntity = socialAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SocialAccount not found with id: " + id));
        return socialAccountPersistenceMapper.socialAccountEntityToSocialAccount(socialAccountEntity);
    }

    @Override
    public List<SocialAccount> findAll() {
        return socialAccountRepository.findAll().stream()
                .map(socialAccountPersistenceMapper::socialAccountEntityToSocialAccount)
                .toList();
    }

    // throw IllegalArgumentException ?
    @Override
    public List<SocialAccount> findByCompanyId(UUID companyId) {
        return socialAccountRepository.findByCompanyId(companyId).stream()
                .map(socialAccountPersistenceMapper::socialAccountEntityToSocialAccount)
                .toList();
    }

    // throw IllegalArgumentException ?
    @Override
    public List<SocialAccount> findByPlatform(SocialAccountPlatform platform) {
        return socialAccountRepository.findByPlatform(platform).stream()
                .map(socialAccountPersistenceMapper::socialAccountEntityToSocialAccount)
                .toList();
    }

    // throw IllegalArgumentException ?
    @Override
    public List<SocialAccount> findByCompanyIdAndPlatform(UUID companyId, SocialAccountPlatform platform) {
        return socialAccountRepository.findByPlatform(platform).stream()
                .map(socialAccountPersistenceMapper::socialAccountEntityToSocialAccount)
                .toList();
    }

    @Override
    public SocialAccount deleteById(UUID id) {
        SocialAccountEntity socialAccountEntity = socialAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SocialAccount not found with id: " + id));
        socialAccountRepository.delete(socialAccountEntity);
        return socialAccountPersistenceMapper.socialAccountEntityToSocialAccount(socialAccountEntity);
    }
}
