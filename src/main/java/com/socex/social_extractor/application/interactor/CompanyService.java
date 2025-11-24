package com.socex.social_extractor.application.interactor;

import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.factory.CompanyFactory;
import com.socex.social_extractor.domain.model.Company;
import com.socex.social_extractor.domain.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService implements CompanyUseCaseFacade {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(CreateCompanyCommand command) {
        String name = command.name();
        if (companyRepository.existsByName(name)) {
            throw new IllegalArgumentException("Company already exists: " + name);
        }

        Company company = CompanyFactory.create(command);

        return companyRepository.save(company);
    }

    @Override
    public Company getCompany(UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company delete(UUID id) {
        return companyRepository.deleteById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));
    }

    @Override
    public Company update(UpdateCompanyCommand command) {
        Company existingCompany = getCompany(command.id());

        if (!existingCompany.name().equals(command.name())
                && companyRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Company name already exists: " + command.name());
        }

        Company updatedCompany = CompanyFactory.update(existingCompany, command);

        return companyRepository.save(updatedCompany);
    }
}
