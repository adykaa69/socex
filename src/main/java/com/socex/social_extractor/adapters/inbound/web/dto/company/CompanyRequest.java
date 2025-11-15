package com.socex.social_extractor.adapters.inbound.web.dto.company;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        String name
) {}
