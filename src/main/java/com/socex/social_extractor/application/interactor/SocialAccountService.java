package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.exception.SocialAccountNotFoundException;
import com.socex.social_extractor.domain.factory.SocialAccountFactory;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import com.socex.social_extractor.domain.repository.SocialAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SocialAccountService implements SocialAccountUseCaseFacade {

    private static final Logger log = LoggerFactory.getLogger(SocialAccountService.class);
    private final SocialAccountRepository socialAccountRepository;

    public SocialAccountService(SocialAccountRepository socialAccountRepository) {
        this.socialAccountRepository = socialAccountRepository;
    }

    @Override
    public SocialAccount create(CreateSocialAccountCommand command) {
        log.debug("Initiating creation of social account. CompanyID: {}, Platform: {}",
                command.companyId(), command.platform());

        SocialAccount socialAccount = SocialAccountFactory.create(command);
        SocialAccount savedSocialAccount = socialAccountRepository.save(socialAccount);

        log.info("Social account created successfully. ID: {}, CompanyID: {}, Platform: {}",
                savedSocialAccount.id(), savedSocialAccount.companyId(), savedSocialAccount.platform());
        return savedSocialAccount;
    }

    @Override
    public SocialAccount getSocialAccountById(UUID id) {
        log.debug("Fetching social account with ID: {}", id);
        return socialAccountRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to lookup social account. Social account with ID '{}' not found", id);
                    return new SocialAccountNotFoundException(id);
                });
    }

    @Override
    public List<SocialAccount> getSocialAccountsByCompanyIdAndPlatform(UUID companyId, SocialAccountPlatform platform) {
        log.debug("Fetching social accounts for CompanyID: {} on Platform: {}", companyId, platform);
        return socialAccountRepository.findByCompanyIdAndPlatform(companyId, platform);
    }

    @Override
    public List<SocialAccount> getAllSocialAccounts() {
        log.debug("Fetching all social accounts");
        List<SocialAccount> socialAccounts = socialAccountRepository.findAll();
        log.info("Retrieved total of {} social accounts.", socialAccounts.size());
        return socialAccounts;
    }

    @Override
    public List<SocialAccount> getSocialAccountsByCompanyId(UUID companyId) {
        log.debug("Fetching social accounts for CompanyID: {}", companyId);
        return socialAccountRepository.findByCompanyId(companyId);
    }

    public List<SocialAccount> getSocialAccountsByPlatform(SocialAccountPlatform platform) {
        log.debug("Fetching social accounts on Platform: {}", platform);
        return socialAccountRepository.findByPlatform(platform);
    }

    @Override
    public SocialAccount delete(UUID id) {
        log.debug("Initiating deletion of social account with ID: {}", id);
        return socialAccountRepository.deleteById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to delete social account. Social account with ID '{}' not found", id);
                    return new SocialAccountNotFoundException(id);
                });
    }

    @Override
    public void deleteSocialAccountsByCompanyId(UUID companyId) {
        log.debug("Initiating deletion of social accounts for CompanyID: {}", companyId);
        socialAccountRepository.deleteAllByCompanyId(companyId);
        log.info("All social accounts for CompanyID: {} have been deleted.", companyId);
    }

    @Override
    public SocialAccount update(UpdateSocialAccountCommand command) {
        log.debug("Initiating update of social account with ID: {}", command.id());
        SocialAccount existingSocialAccount = getSocialAccountById(command.id());

        SocialAccount updatedSocialAccount = SocialAccountFactory.update(existingSocialAccount, command);
        SocialAccount savedSocialAccount = socialAccountRepository.save(updatedSocialAccount);

        log.info("Social account updated successfully. ID: {}", savedSocialAccount.id());
        return savedSocialAccount;
    }
}
