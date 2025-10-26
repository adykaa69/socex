package com.socex.social_extractor.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Company(
        UUID id,
        String name,
        Instant createdAt
) {

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {
        private UUID id;
        private String name;
        private Instant createdAt;

        public CompanyBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Company build() {
            return new Company(id, name, createdAt);
        }
    }
}


