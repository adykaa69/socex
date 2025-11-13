package com.socex.social_extractor.adapters.inbound.web;

import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSocialAccountPlatformConverter implements Converter<String, SocialAccountPlatform> {

    @Override
    public SocialAccountPlatform convert(String source) {
        return SocialAccountPlatform.fromString(source);
    }
}
