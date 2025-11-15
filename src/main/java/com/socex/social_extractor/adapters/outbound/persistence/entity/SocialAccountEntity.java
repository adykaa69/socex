package com.socex.social_extractor.adapters.outbound.persistence.entity;

import com.socex.social_extractor.domain.model.SocialAccountPlatform;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "social_account")
public class SocialAccountEntity {

    @Id
    @Column(nullable = false,  updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private SocialAccountPlatform platform;

    @Column(name = "account_url",  nullable = false)
    private String accountUrl;

    @Column(name = "account_id")
    private String accountId;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "company_id",  nullable = false)
    private CompanyEntity company;

    public SocialAccountEntity() {}

    public UUID getId () {
        return id;
    }

    public void setId (UUID id) {
        this.id = id;
    }

    public SocialAccountPlatform getPlatform () {
        return platform;
    }

    public void setPlatform (SocialAccountPlatform platform) {
        this.platform = platform;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
