package com.socex.social_extractor.adapters.inbound.web.mapper;

import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyResponse;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyWebMapper {
    CreateCompanyCommand companyRequestToCreateCompanyCommand(CompanyRequest companyRequest);
    Company companyRequestToCompany(CompanyRequest companyRequest);
    CompanyResponse companyToCompanyResponse(Company company);

}
