package com.socex.social_extractor.adapters.inbound.web.controller;

import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyRequest;
import com.socex.social_extractor.adapters.inbound.web.dto.company.CompanyResponse;
import com.socex.social_extractor.adapters.inbound.web.dto.platform.PlatformResponse;
import com.socex.social_extractor.adapters.inbound.web.mapper.CompanyWebMapper;
import com.socex.social_extractor.application.service.company.CompanyUseCaseFacade;
import com.socex.social_extractor.application.service.company.command.CreateCompanyCommand;
import com.socex.social_extractor.application.service.company.command.UpdateCompanyCommand;
import com.socex.social_extractor.domain.model.Company;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyUseCaseFacade companyUseCaseFacade;
    private final CompanyWebMapper companyWebMapper;

    public CompanyController(CompanyUseCaseFacade companyUseCaseFacade,
                             CompanyWebMapper companyWebMapper) {
        this.companyUseCaseFacade = companyUseCaseFacade;
        this.companyWebMapper = companyWebMapper;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CompanyResponse> getCompany(@PathVariable UUID id) {
        Company company = companyUseCaseFacade.getCompany(id);
        CompanyResponse companyResponse = companyWebMapper.companyToCompanyResponse(company);
        return new PlatformResponse<>("success", "Company retrieved successfully", companyResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<CompanyResponse>> getAllCompanies() {
        List<Company> companies = companyUseCaseFacade.getAllCompanies();
        List<CompanyResponse> companyResponses = companies.stream()
                .map(companyWebMapper::companyToCompanyResponse)
                .toList();
        return new PlatformResponse<>("success", "All companies retrieved successfully", companyResponses);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        CreateCompanyCommand command =
                companyWebMapper.companyRequestToCreateCompanyCommand(companyRequest);
        Company savedCompany = companyUseCaseFacade.create(command);
        CompanyResponse companyResponse =
                companyWebMapper.companyToCompanyResponse(savedCompany);
        return new PlatformResponse<>("success", "Company created successfully", companyResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CompanyResponse> deleteCompany(@PathVariable UUID id) {
        Company deletedCompany = companyUseCaseFacade.delete(id);
        CompanyResponse companyResponse = companyWebMapper.companyToCompanyResponse(deletedCompany);
        return new PlatformResponse<>("success", "Company deleted successfully", companyResponse);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CompanyResponse> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequest companyRequest
    ) {
        UpdateCompanyCommand command =
                companyWebMapper.companyRequestToUpdateCompanyCommand(id, companyRequest);
        Company updatedCompany = companyUseCaseFacade.update(command);
        CompanyResponse companyResponse =
                companyWebMapper.companyToCompanyResponse(updatedCompany);
        return new PlatformResponse<>("success", "Company updated successfully", companyResponse);
    }

}
