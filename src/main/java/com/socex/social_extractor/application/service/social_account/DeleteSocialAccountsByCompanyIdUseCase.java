package com.socex.social_extractor.application.service.social_account;

import java.util.UUID;

public interface DeleteSocialAccountsByCompanyIdUseCase {
    void deleteSocialAccountsByCompanyId(UUID companyId);
}
