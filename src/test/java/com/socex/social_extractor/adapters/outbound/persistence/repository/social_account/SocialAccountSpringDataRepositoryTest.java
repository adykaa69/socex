package com.socex.social_extractor.adapters.outbound.persistence.repository.social_account;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import com.socex.social_extractor.adapters.outbound.persistence.entity.SocialAccountEntity;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SocialAccountSpringDataRepositoryTest {

    @Autowired
    private SocialAccountSpringDataRepository socialAccountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByCompanyId_ShouldReturnAccounts_ForGivenCompany() {
        // Given
        CompanyEntity company = createAndPersistCompany();
        createAndPersistAccount(company, SocialAccountPlatform.FACEBOOK);
        createAndPersistAccount(company, SocialAccountPlatform.TIKTOK);

        // When
        List<SocialAccountEntity> results = socialAccountRepository.findByCompanyId(company.getId());

        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getPlatform()).isEqualTo(SocialAccountPlatform.FACEBOOK);
        assertThat(results.get(1).getPlatform()).isEqualTo(SocialAccountPlatform.TIKTOK);
    }

    @Test
    void findByPlatform_ShouldReturnAccounts_ForGivenPlatform() {
        // Given
        CompanyEntity company = createAndPersistCompany();
        createAndPersistAccount(company, SocialAccountPlatform.FACEBOOK);
        createAndPersistAccount(company, SocialAccountPlatform.LINKEDIN);

        // When
        List<SocialAccountEntity> results = socialAccountRepository.findByPlatform(SocialAccountPlatform.FACEBOOK);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPlatform()).isEqualTo(SocialAccountPlatform.FACEBOOK);
    }

    @Test
    void findByCompanyIdAndPlatform_ShouldReturnSpecificAccount() {
        // Given
        CompanyEntity targetCompany = createAndPersistCompany();
        CompanyEntity otherCompany = createAndPersistCompany();

        // 1. Correct Match (Target Company + Target Platform)
        createAndPersistAccount(targetCompany, SocialAccountPlatform.FACEBOOK);

        // 2. Wrong Platform (Target Company + Wrong Platform)
        createAndPersistAccount(targetCompany, SocialAccountPlatform.TIKTOK);

        // 3. Wrong Company (Wrong Company + Target Platform)
        createAndPersistAccount(otherCompany, SocialAccountPlatform.FACEBOOK);

        // When
        List<SocialAccountEntity> results = socialAccountRepository.findByCompanyIdAndPlatform(
                targetCompany.getId(),
                SocialAccountPlatform.FACEBOOK
        );

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCompany().getId()).isEqualTo(targetCompany.getId());
        assertThat(results.get(0).getPlatform()).isEqualTo(SocialAccountPlatform.FACEBOOK);
    }

    @Test
    void deleteAllByCompanyId_ShouldRemoveOnlyTargetCompanyAccounts() {
        // Given
        CompanyEntity company1 = createAndPersistCompany();
        CompanyEntity company2 = createAndPersistCompany();

        createAndPersistAccount(company1, SocialAccountPlatform.FACEBOOK); // To be deleted
        createAndPersistAccount(company2, SocialAccountPlatform.FACEBOOK); // Should remain

        // When
        socialAccountRepository.deleteAllByCompanyId(company1.getId());
        entityManager.flush();
        entityManager.clear(); // Clear cache to force DB fetch

        // Then
        List<SocialAccountEntity> remaining = socialAccountRepository.findAll();
        assertThat(remaining).hasSize(1);
        assertThat(remaining.get(0).getCompany().getId()).isEqualTo(company2.getId());
    }


    private CompanyEntity createAndPersistCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setId(UUID.randomUUID());
        company.setName("Company " + UUID.randomUUID());
        company.setCreatedAt(Instant.now());
        return entityManager.persistAndFlush(company);
    }

    private void createAndPersistAccount(CompanyEntity company, SocialAccountPlatform platform) {
        SocialAccountEntity account = new SocialAccountEntity();
        account.setId(UUID.randomUUID());
        account.setCompany(company);
        account.setPlatform(platform);
        account.setAccountUrl("http://url");
        account.setAccountId("acc_id");
        account.setCreatedAt(Instant.now());
        entityManager.persistAndFlush(account);
    }
}