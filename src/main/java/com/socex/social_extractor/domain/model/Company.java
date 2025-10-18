package com.socex.social_extractor.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Company {
    private final UUID id;
    private final String name;
    private final Instant createdAt;

    public Company(UUID id, String name, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public  String getName() { return name; }
    public Instant getCreatedAt() { return createdAt; }

    public static Company ofNew(String name) {
        Objects.requireNonNull(name, "Company name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be blank");
        }
        return new Company(UUID.randomUUID(), name, Instant.now());
    }
}


