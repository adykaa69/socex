package com.socex.social_extractor.application.service.orchestrator;

import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.model.SocialAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ServiceOrchestrator {

    private final CompanyUseCaseFacade companyService;
    private final SocialAccountUseCaseFacade socialAccountService;

    public ServiceOrchestrator(CompanyUseCaseFacade companyService,
                               SocialAccountUseCaseFacade socialAccountService) {
        this.companyService = companyService;
        this.socialAccountService = socialAccountService;
    }

    @Transactional
    public SocialAccount createSocialAccount(CreateSocialAccountCommand command) {
        // Company existence validation
        companyService.getCompany(command.companyId());
        return socialAccountService.create(command);
    }

    @Transactional
    public Company deleteCompany (UUID companyId) {
        socialAccountService.deleteSocialAccountsByCompanyId(companyId);
        return companyService.delete(companyId);
    }
}
