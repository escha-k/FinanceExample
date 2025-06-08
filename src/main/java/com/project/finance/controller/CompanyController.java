package com.project.finance.controller;

import com.project.finance.constants.CacheKey;
import com.project.finance.domain.Company;
import com.project.finance.dto.CompanyDto;
import com.project.finance.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final Trie<String, String> trie;

    private final CompanyService companyService;
    private final RedisCacheManager redisCacheManager;

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Page<Company>> getCompanies(final Pageable pageable) {
        Page<Company> companies = companyService.findAll(pageable);

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> getAutoComplete(@RequestParam String keyword) {
        List<String> result = companyService.autoComplete(keyword);

        return ResponseEntity.ok(result);
    }


    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<CompanyDto> postCompany(@RequestBody CompanyDto companyDto) {
        String ticker = companyDto.getTicker();
        if (ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker is empty");
        }

        CompanyDto resultDto = companyService.save(ticker);
        companyService.addAutoCompleteKeyword(resultDto.getName());

        return ResponseEntity.ok(resultDto);
    }

    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<String> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompany(ticker);
        clearFinanceCache(companyName);

        return ResponseEntity.ok(companyName);
    }

    private void clearFinanceCache(String companyName) {
        redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }

}
