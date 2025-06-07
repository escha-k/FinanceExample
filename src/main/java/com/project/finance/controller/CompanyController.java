package com.project.finance.controller;

import com.project.finance.domain.Company;
import com.project.finance.dto.CompanyDto;
import com.project.finance.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final Trie<String, String> trie;

    private final CompanyService companyService;

    @GetMapping
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
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        return ResponseEntity.ok("");
    }

}
