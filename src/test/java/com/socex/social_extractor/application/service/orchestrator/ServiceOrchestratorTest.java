package com.socex.social_extractor.application.service.orchestrator;

import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.SocialAccountUseCaseFacade;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class ServiceOrchestratorTest {

    @Mock
    private CompanyUseCaseFacade companyService;

    @Mock
    private SocialAccountUseCaseFacade socialAccountService;

    @InjectMocks
    private ServiceOrchestrator underTest;

    @Test
    void createSocialAccount_ShouldValidateCompanyExists_BeforeCreation() {
        // Given
        UUID companyId = UUID.randomUUID();
        CreateSocialAccountCommand command = new CreateSocialAccountCommand(
                companyId, SocialAccountPlatform.TIKTOK, "url", "id");

        // When
        underTest.createSocialAccount(command);

        // Then
        InOrder inOrder = inOrder(companyService, socialAccountService);

        inOrder.verify(companyService).getCompany(companyId);
        inOrder.verify(socialAccountService).create(command);
    }

    @Test
    void deleteCompany_ShouldDeleteAccounts_BeforeDeletingCompany() {
        // Given
        UUID companyId = UUID.randomUUID();

        // When
        underTest.deleteCompany(companyId);

        // Then
        InOrder inOrder = inOrder(socialAccountService, companyService);

        inOrder.verify(socialAccountService).deleteSocialAccountsByCompanyId(companyId);
        inOrder.verify(companyService).delete(companyId);
    }
}