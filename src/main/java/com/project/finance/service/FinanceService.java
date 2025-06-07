package com.project.finance.service;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.DividendDto;
import com.project.finance.dto.ScrapResult;
import com.project.finance.repository.CompanyRepository;
import com.project.finance.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapResult getDividendByCompanyName(String companyName) {

        Company company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        CompanyDto companyDto = CompanyDto.builder()
                .name(company.getName())
                .ticker(company.getTicker())
                .build();

        List<Dividend> dividends = dividendRepository.findAllByCompany(company);
        List<DividendDto> dividendDtoList = dividends.stream()
                .map(e -> DividendDto.builder()
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build())
                .toList();

        ScrapResult scrapResult = new ScrapResult();
        scrapResult.setCompany(companyDto);
        scrapResult.setDividendList(dividendDtoList);

        return scrapResult;
    }
}
