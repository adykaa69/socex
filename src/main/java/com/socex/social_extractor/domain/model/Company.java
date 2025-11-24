package com.socex.social_extractor.domain.model;

import com.socex.social_extractor.domain.shared.DomainValidator;

import java.time.Instant;
import java.util.UUID;

public record Company(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {

    public Company {
        DomainValidator.notNullOrBlank(name, "Company name");
        DomainValidator.notNull(id, "Company ID");
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {
        private UUID id;
        private String name;
        private Instant createdAt;
        private Instant updatedAt;

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

        public CompanyBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Company build() {
            return new Company(id, name, createdAt, updatedAt);
        }
    }
}


