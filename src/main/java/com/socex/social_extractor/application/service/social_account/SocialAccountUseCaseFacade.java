package com.socex.social_extractor.application.service.social_account;

public interface SocialAccountUseCaseFacade extends
        CreateSocialAccountUseCase,
        DeleteSocialAccountUseCase,
        ListSocialAccountsByCompanyIdAndPlatformUseCase,
        GetSocialAccountByIdUseCase,
        ListAllSocialAccountsUseCase,
        ListSocialAccountsByCompanyIdUseCase,
        ListSocialAccountsByPlatformUseCase,
        UpdateSocialAccountUseCase {}
