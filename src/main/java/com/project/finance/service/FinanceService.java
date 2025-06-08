package com.project.finance.service;

import com.project.finance.constants.CacheKey;
import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.DividendDto;
import com.project.finance.dto.ScrapResult;
import com.project.finance.exception.impl.NoCompanyException;
import com.project.finance.repository.CompanyRepository;
import com.project.finance.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapResult getDividendByCompanyName(String companyName) {
        Company company = companyRepository.findByName(companyName)
                .orElseThrow(NoCompanyException::new);
        CompanyDto companyDto = CompanyDto.builder()
                .name(company.getName())
                .ticker(company.getTicker())
                .build();

        List<Dividend> dividends = dividendRepository.findAllByCompany(company);
        List<DividendDto> dividendDtoList = dividends.stream()
                .map(entity -> DividendDto.builder()
                        .date(entity.getDate())
                        .dividend(entity.getDividend())
                        .build())
                .toList();

        ScrapResult scrapResult = new ScrapResult();
        scrapResult.setCompany(companyDto);
        scrapResult.setDividendList(dividendDtoList);

        return scrapResult;
    }
}
