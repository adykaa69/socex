package com.socex.social_extractor.adapters.inbound.web.mapper;

import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.social_account.SocialAccountResponse;
import com.socex.social_extractor.application.service.social_account.command.CreateSocialAccountCommand;
import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses= CompanyWebMapper.class)
public interface SocialAccountWebMapper {
    CreateSocialAccountCommand socialAccountRequestToCreateSocialAccountCommand(SocialAccountRequest socialAccountRequest);
    UpdateSocialAccountCommand socialAccountRequestToUpdateSocialAccountCommand(UUID id, SocialAccountRequest socialAccountRequest);
    @Mapping(source = "company.id", target = "companyId")
    SocialAccountResponse socialAccountToSocialAccountResponse(SocialAccount socialAccount);
}
