package com.socex.social_extractor.adapters.inbound.web.mapper;

import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyResponse;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CompanyWebMapper {
    CreateCompanyCommand companyRequestToCreateCompanyCommand(CompanyRequest companyRequest);
    UpdateCompanyCommand companyRequestToUpdateCompanyCommand(UUID id, CompanyRequest companyRequest);
    CompanyResponse companyToCompanyResponse(Company company);

}
