package com.socex.social_extractor.adapters.outbound.persistence.repository;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import com.socex.social_extractor.adapters.outbound.persistence.mapper.CompanyPersistenceMapper;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.repository.CompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CompanyPersistenceAdapter implements CompanyRepository {

    private final SpringDataCompanyRepository companyRepository;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    public CompanyPersistenceAdapter(SpringDataCompanyRepository companyRepository,
                                     CompanyPersistenceMapper companyPersistenceMapper) {
        this.companyRepository = companyRepository;
        this.companyPersistenceMapper = companyPersistenceMapper;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity companyEntity = companyPersistenceMapper.companyToCompanyEntity(company);
        CompanyEntity savedEntity = companyRepository.save(companyEntity);
        return companyPersistenceMapper.companyEntityToCompany(savedEntity);
    }

    @Override
    public Company findById(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
        return companyPersistenceMapper.companyEntityToCompany(companyEntity);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll().stream()
                .map(companyPersistenceMapper::companyEntityToCompany)
                .toList();
    }

    @Override
    public Company deleteById(UUID id) {
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
        companyRepository.delete(companyEntity);
        return companyPersistenceMapper.companyEntityToCompany(companyEntity);
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
