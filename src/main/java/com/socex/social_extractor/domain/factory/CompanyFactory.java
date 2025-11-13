package com.socex.social_extractor.domain.factory;

import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;

import java.util.UUID;

public class CompanyFactory {

    public static Company create(CreateCompanyCommand command) {
        return Company.builder()
                .id(UUID.randomUUID())
                .name(command.name())
                .build();
    }
}
