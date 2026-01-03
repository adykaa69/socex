package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.domain.exception.SocialAccountNotFoundException;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import com.socex.social_extractor.domain.repository.SocialAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SocialAccountServiceTest {

    @Mock
    private SocialAccountRepository socialAccountRepository;

    @InjectMocks
    private SocialAccountService underTest;

    @Test
    void create_ShouldSaveAndReturnSocialAccount() {
        // Given
        CreateSocialAccountCommand command = new CreateSocialAccountCommand(
                UUID.randomUUID(), SocialAccountPlatform.FACEBOOK, "http://url", "accId"
        );
        given(socialAccountRepository.save(any(SocialAccount.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        SocialAccount result = underTest.create(command);

        // Then
        assertThat(result.platform()).isEqualTo(SocialAccountPlatform.FACEBOOK);
        verify(socialAccountRepository).save(any(SocialAccount.class));
    }

    @Test
    void delete_ShouldThrowSocialAccountNotFoundException_WhenIdNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        given(socialAccountRepository.deleteById(id)).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> underTest.delete(id))
                .isInstanceOf(SocialAccountNotFoundException.class)
                .hasMessage("SocialAccount not found with id: %s", id);
    }

    @Test
    void deleteSocialAccountsByCompanyId_ShouldDelegateToRepository() {
        // Given
        UUID companyId = UUID.randomUUID();

        // When
        underTest.deleteSocialAccountsByCompanyId(companyId);

        // Then
        verify(socialAccountRepository).deleteAllByCompanyId(companyId);
    }

}