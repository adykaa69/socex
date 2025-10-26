package com.socex.social_extractor.adapters.outbound.persistence.mapper;

import com.socex.social_extractor.adapters.outbound.persistence.entity.SocialAccountEntity;
import com.socex.social_extractor.domain.model.SocialAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CompanyPersistenceMapper.class)
public interface SocialAccountPersistenceMapper {

//    @Mapping(source = "companyId", target = "company.id")
    SocialAccountEntity socialAccountToSocialAccountEntity(SocialAccount socialAccount);

//    @Mapping(source = "companyId", target = "company.id")
    SocialAccount socialAccountEntityToSocialAccount(SocialAccountEntity socialAccountEntity);
}
