package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.exception.CompanyAlreadyExistsException;
import com.socex.social_extractor.domain.exception.CompanyNotFoundException;
import com.socex.social_extractor.domain.factory.CompanyFactory;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService implements CompanyUseCaseFacade {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(CreateCompanyCommand command) {
        String name = command.name();
        log.debug("Initiating creation of company with name: {}", name);

        if (companyRepository.existsByName(name)) {
            log.warn("Failed to create company. Company with name '{}' already exists", name);
            throw new CompanyAlreadyExistsException(name);
        }

        Company company = CompanyFactory.create(command);
        Company savedCompany = companyRepository.save(company);

        log.info("Company created successfully. ID: {}, Name: {}", savedCompany.id(), savedCompany.name());
        return savedCompany;
    }

    @Override
    public Company getCompany(UUID id) {
        log.debug("Fetching company with ID: {}", id);
        return companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to lookup company. Company with ID '{}' not found", id);
                    return new CompanyNotFoundException(id);
                });
    }

    @Override
    public List<Company> getAllCompanies() {
        log.debug("Fetching all companies");
        List<Company> companies = companyRepository.findAll();
        log.info("Retrieved total of {} companies.", companies.size());
        return companies;
    }

    @Override
    public Company delete(UUID id) {
        log.info("Initiating deletion of company with ID: {}", id);
        return companyRepository.deleteById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to delete company. Company with ID '{}' not found", id);
                    return new CompanyNotFoundException(id);
                });
    }

    @Override
    public Company update(UpdateCompanyCommand command) {
        log.debug("Initiating update of company with ID: {}", command.id());

        Company existingCompany = getCompany(command.id());

        if (!existingCompany.name().equals(command.name())
                && companyRepository.existsByName(command.name())) {
            log.warn("Failed to update company. Company with name '{}' already exists", command.name());
            throw new CompanyAlreadyExistsException(command.name());
        }

        Company updatedCompany = CompanyFactory.update(existingCompany, command);
        Company savedCompany = companyRepository.save(updatedCompany);

        log.info("Company updated successfully. ID: {}, Name: {}", savedCompany.id(), savedCompany.name());
        return savedCompany;
    }
}
