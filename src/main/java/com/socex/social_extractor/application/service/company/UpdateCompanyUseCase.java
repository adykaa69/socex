package com.socex.social_extractor.application.service.company;

import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;

public interface UpdateCompanyUseCase {
    Company update(UpdateCompanyCommand command);
}
