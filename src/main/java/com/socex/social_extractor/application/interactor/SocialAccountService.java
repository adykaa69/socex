package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.exception.SocialAccountNotFoundException;
import com.socex.social_extractor.domain.factory.SocialAccountFactory;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import com.socex.social_extractor.domain.repository.SocialAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SocialAccountService implements SocialAccountUseCaseFacade {

    private final SocialAccountRepository socialAccountRepository;

    public SocialAccountService(SocialAccountRepository socialAccountRepository) {
        this.socialAccountRepository = socialAccountRepository;
    }

    @Override
    public SocialAccount create(CreateSocialAccountCommand command) {
        SocialAccount socialAccount = SocialAccountFactory.create(command);
        return socialAccountRepository.save(socialAccount);
    }

    @Override
    public SocialAccount getSocialAccountById(UUID id) {
        return socialAccountRepository.findById(id)
                .orElseThrow(() -> new SocialAccountNotFoundException(id));
    }

    @Override
    public List<SocialAccount> getSocialAccountsByCompanyIdAndPlatform(UUID companyId, SocialAccountPlatform platform) {
        return socialAccountRepository.findByCompanyIdAndPlatform(companyId, platform);
    }

    @Override
    public List<SocialAccount> getAllSocialAccounts() {
        return socialAccountRepository.findAll();
    }

    @Override
    public List<SocialAccount> getSocialAccountsByCompanyId(UUID companyId) {
        return socialAccountRepository.findByCompanyId(companyId);
    }

    public List<SocialAccount> getSocialAccountsByPlatform(SocialAccountPlatform platform) {
        return socialAccountRepository.findByPlatform(platform);
    }

    @Override
    public SocialAccount delete(UUID id) {
        return socialAccountRepository.deleteById(id)
                .orElseThrow(() -> new SocialAccountNotFoundException(id));
    }

    @Override
    public SocialAccount update(UpdateSocialAccountCommand command) {
        SocialAccount existingSocialAccount = getSocialAccountById(command.id());

        SocialAccount updatedSocialAccount = SocialAccountFactory.update(existingSocialAccount, command);

        return socialAccountRepository.save(updatedSocialAccount);
    }
}
