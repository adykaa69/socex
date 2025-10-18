package com.socex.social_extractor.application.service.company;

import com.socex.social_extractor.domain.model.Company;

import java.util.UUID;

public interface GetCompanyUseCase {
    Company getCompany(UUID id);
}
