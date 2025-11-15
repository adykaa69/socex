package com.socex.social_extractor.application.service.company;

import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;

public interface CreateCompanyUseCase {
    Company create(CreateCompanyCommand command);
}
