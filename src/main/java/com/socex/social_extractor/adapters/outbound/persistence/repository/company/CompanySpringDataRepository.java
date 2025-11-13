package com.socex.social_extractor.adapters.outbound.persistence.repository.company;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanySpringDataRepository extends JpaRepository<CompanyEntity, UUID> {
    boolean existsByName(String name);
}
