package com.socex.social_extractor.adapters.outbound.persistence.repository.company;

import com.socex.social_extractor.adapters.outbound.persistence.entity.CompanyEntity;
import com.socex.social_extractor.adapters.outbound.persistence.mapper.CompanyPersistenceMapper;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.repository.CompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyPersistenceAdapter implements CompanyRepository {

    private final CompanySpringDataRepository companyRepository;
    private final CompanyPersistenceMapper companyPersistenceMapper;

    public CompanyPersistenceAdapter(CompanySpringDataRepository companyRepository,
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
    public Optional<Company> findById(UUID id) {
        return companyRepository.findById(id)
                .map(companyPersistenceMapper::companyEntityToCompany);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll().stream()
                .map(companyPersistenceMapper::companyEntityToCompany)
                .toList();
    }

    @Override
    public Optional<Company> deleteById(UUID id) {
        return companyRepository.findById(id)
                .map(company -> {
                    companyRepository.delete(company);
                    return companyPersistenceMapper.companyEntityToCompany(company);
                });
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
