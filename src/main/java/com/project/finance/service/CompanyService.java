package com.project.finance.service;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.ScrapResult;
import com.project.finance.exception.impl.AlreadyExistsCompanyException;
import com.project.finance.exception.impl.InvalidTickerException;
import com.project.finance.exception.impl.NoCompanyException;
import com.project.finance.repository.CompanyRepository;
import com.project.finance.repository.DividendRepository;
import com.project.finance.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Scraper scraper;
    private final Trie<String, String> trie;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public CompanyDto save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new AlreadyExistsCompanyException();
        }

        CompanyDto companyDto = storeCompanyAndDividend(ticker);

        log.info("insert new company info: {}", companyDto.getName());

        return companyDto;
    }

    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public String deleteCompany(String ticker) {
        Company deletedCompany = companyRepository.findByTicker(ticker)
                .orElseThrow(NoCompanyException::new);

        dividendRepository.deleteAllByCompany(deletedCompany);
        companyRepository.delete(deletedCompany);
        deleteAutoCompleteKeyword(deletedCompany.getName());

        log.info("delete company info: {}", deletedCompany.getName());

        return deletedCompany.getName();
    }

    public void addAutoCompleteKeyword(String keyword) {
        trie.put(keyword, null);
    }

    public List<String> autoComplete(String keyword) {
        return trie.prefixMap(keyword).keySet().stream().toList();
    }

    public void deleteAutoCompleteKeyword(String keyword) {
        trie.remove(keyword);
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        CompanyDto resultDto = scraper.scrapCompanyByTicker(ticker);
        if (resultDto == null) {
            throw new InvalidTickerException(ticker);
        }

        ScrapResult scrapResult = scraper.scrap(resultDto);

        Company savedCompany = companyRepository.save(new Company(resultDto));
        List<Dividend> list = scrapResult.getDividendList().stream()
                .map(dto -> new Dividend(savedCompany, dto))
                .toList();
        dividendRepository.saveAll(list);

        return resultDto;
    }
}
