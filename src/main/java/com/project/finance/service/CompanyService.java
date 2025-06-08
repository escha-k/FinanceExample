package com.project.finance.service;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.ScrapResult;
import com.project.finance.repository.CompanyRepository;
import com.project.finance.repository.DividendRepository;
import com.project.finance.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Scraper scraper;
    private final Trie<String, String> trie;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public CompanyDto save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new RuntimeException("company already exists");
        }

        return storeCompanyAndDividend(ticker);
    }

    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public String deleteCompany(String ticker) {
        Company deletedCompany = companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("company not found"));

        dividendRepository.deleteAllByCompany(deletedCompany);
        companyRepository.delete(deletedCompany);
        deleteAutoCompleteKeyword(deletedCompany.getName());

        return deletedCompany.getName();
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        CompanyDto resultDto = scraper.scrapCompanyByTicker(ticker);
        if (resultDto == null) {
            throw new RuntimeException("failed to scrap company by ticker: " + ticker);
        }

        ScrapResult scrapResult = scraper.scrap(resultDto);

        Company savedCompany = companyRepository.save(new Company(resultDto));
        List<Dividend> list = scrapResult.getDividendList().stream()
                .map(dto -> new Dividend(savedCompany, dto))
                .toList();
        dividendRepository.saveAll(list);

        return resultDto;
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
}
