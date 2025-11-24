package com.socex.social_extractor.application.service.company.command;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID id,
        String name
) {}
