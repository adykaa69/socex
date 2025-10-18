package com.socex.social_extractor.adapters.outbound.persistence.mapper;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import com.socex.social_extractor.domain.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyPersistenceMapper {

    CompanyEntity companyToCompanyEntity(Company company);

    Company companyEntityToCompany(CompanyEntity companyEntity);
}
