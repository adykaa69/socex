package com.socex.social_extractor.adapters.inbound.web.mapper;

import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyResponse;
import com.socex.social_extractor.domain.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyWebMapper {

    CompanyResponse companyToCompanyResponse(Company company);

    Company companyRequestToCompany(CompanyRequest companyRequest);
}
