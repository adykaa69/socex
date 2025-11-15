package com.socex.social_extractor.domain.model;

import com.socex.social_extractor.domain.shared.DomainValidator;

import java.time.Instant;
import java.util.UUID;

public record SocialAccount(
        UUID id,
        Company company,
        SocialAccountPlatform platform,
        String accountUrl,
        String accountId,
        Instant createdAt,
        Instant updatedAt
) {

    public SocialAccount {
        DomainValidator.notNull(company, "Company");
        DomainValidator.notNull(platform, "Platform");
        DomainValidator.notNullOrBlank(accountUrl, "Account URL");
    }

    public static SocialAccountBuilder builder() {
        return new SocialAccountBuilder();
    }

    public static class SocialAccountBuilder {
        private UUID id;
        private Company company;
        private SocialAccountPlatform platform;
        private String accountUrl;
        private String accountId;
        private Instant createdAt;
        private Instant updatedAt;

        public SocialAccountBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public SocialAccountBuilder company(Company company) {
            this.company = company;
            return this;
        }

        public SocialAccountBuilder platform(SocialAccountPlatform platform) {
            this.platform = platform;
            return this;
        }

        public SocialAccountBuilder accountUrl(String accountUrl) {
            this.accountUrl = accountUrl;
            return this;
        }

        public SocialAccountBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public SocialAccountBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SocialAccountBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public SocialAccount build() {
            return new SocialAccount(id, company, platform, accountUrl, accountId, createdAt, updatedAt);
        }
    }
}
