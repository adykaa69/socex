package com.socex.social_extractor.domain.model;

import java.time.Instant;
import java.util.UUID;

public record SocialAccount(
        UUID id,
        Company company,
        SocialAccountPlatform platform,
        String accountUrl,
        String accountId,
        Instant createdAt
) {

//    private final UUID id;
//    private final UUID companyId;
//    private final SocialAccountPlatform platform;
//    private final String accountUrl;
//    private final String accountId;
//    private final Instant createdAt;
//
//    private SocialAccount(UUID id, UUID companyId, SocialAccountPlatform platform, String accountUrl, String accountId, Instant createdAt) {
//        this.id = id;
//        this.companyId = DomainValidator.notNull(companyId, "companyId");
//        this.platform = DomainValidator.notNull(platform, "platform");
//        this.accountUrl = DomainValidator.notNullOrBlank(accountUrl, "accountUrl");
//        this.accountId = accountId;
//        this.createdAt = createdAt;
//    }

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

        public SocialAccount build() {
            return new SocialAccount(id, company, platform, accountUrl, accountId, createdAt);
        }
    }
}
