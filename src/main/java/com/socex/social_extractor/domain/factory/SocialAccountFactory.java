package com.socex.social_extractor.domain.factory;

import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.model.SocialAccount;

import java.util.UUID;

public class SocialAccountFactory {

    public static SocialAccount create(CreateSocialAccountCommand command) {
        Company company = Company.builder()
                .id(command.companyId())
                .build();

        return SocialAccount.builder()
                .id(UUID.randomUUID())
                .company(company)
                .platform(command.platform())
                .accountUrl(command.accountUrl())
                .accountId(command.accountId())
                .build();
    }
}
