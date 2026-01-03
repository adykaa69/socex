package com.socex.social_extractor.domain.factory;

import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;
import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SocialAccountFactoryTest {

    @Test
    void create_ShouldReturnNewSocialAccount_FromCommand() {
        // Given
        UUID companyId = UUID.randomUUID();
        CreateSocialAccountCommand command = new CreateSocialAccountCommand(
                companyId,
                SocialAccountPlatform.FACEBOOK,
                "https://facebook.com/page",
                "fb_123"
        );

        // When
        SocialAccount account = SocialAccountFactory.create(command);

        // Then
        assertThat(account.id()).isNotNull();
        assertThat(account.companyId()).isEqualTo(companyId);
        assertThat(account.platform()).isEqualTo(SocialAccountPlatform.FACEBOOK);
        assertThat(account.accountUrl()).isEqualTo("https://facebook.com/page");
        assertThat(account.accountId()).isEqualTo("fb_123");
    }

    @Test
    void update_ShouldReturnUpdatedSocialAccount_BasedOnCommand() {
        // Given
        UUID id = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        SocialAccount existingAccount = new SocialAccount(
                id,
                companyId,
                SocialAccountPlatform.YOUTUBE,
                "https://youtube.com/old_page",
                "yt_456",
                createdAt,
                null
        );

        UpdateSocialAccountCommand command = new UpdateSocialAccountCommand(
                companyId,
                SocialAccountPlatform.YOUTUBE,
                "https://youtube.com/new_page",
                "yt_789"
        );

        // When
        SocialAccount updatedAccount = SocialAccountFactory.update(existingAccount, command);

        // Then
        assertThat(updatedAccount.id()).isEqualTo(id);
        assertThat(updatedAccount.companyId()).isEqualTo(companyId);
        assertThat(updatedAccount.platform()).isEqualTo(SocialAccountPlatform.YOUTUBE);
        assertThat(updatedAccount.accountUrl()).isEqualTo("https://youtube.com/new_page");
        assertThat(updatedAccount.accountId()).isEqualTo("yt_789");
        assertThat(updatedAccount.createdAt()).isEqualTo(createdAt);
    }
}