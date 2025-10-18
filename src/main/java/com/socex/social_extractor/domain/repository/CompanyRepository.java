package com.socex.social_extractor.domain.repository;

import com.socex.social_extractor.domain.model.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    Company findById(UUID id);
    List<Company> findAll();
    Company deleteById(UUID id);
    boolean existsByName(String name);
}
