package com.socex.social_extractor.domain.factory;

import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;

import java.util.UUID;

public class SocialAccountFactory {

    public static SocialAccount create(CreateSocialAccountCommand command) {
        return SocialAccount.builder()
                .id(UUID.randomUUID())
                .companyId(command.companyId())
                .platform(command.platform())
                .accountUrl(command.accountUrl())
                .accountId(command.accountId())
                .build();
    }

    public static SocialAccount update(SocialAccount existingSocialAccount, UpdateSocialAccountCommand command) {
        return SocialAccount.builder()
                .id(existingSocialAccount.id())
                .companyId(existingSocialAccount.companyId())
                .platform(command.platform())
                .accountUrl(command.accountUrl())
                .accountId(command.accountId())
                .createdAt(existingSocialAccount.createdAt())
                .build();
    }
}
