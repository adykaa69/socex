package com.socex.social_extractor.application.service.social_account;

import com.socex.social_extractor.application.service.social_account.command.UpdateSocialAccountCommand;
import com.socex.social_extractor.domain.model.SocialAccount;

public interface UpdateSocialAccountUseCase {
    SocialAccount update(UpdateSocialAccountCommand command);
}
