package com.project.finance.controller;

import com.project.finance.dto.ScrapResult;
import com.project.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<ScrapResult> getFinance(@PathVariable String companyName) {

        return ResponseEntity.ok(financeService.getDividendByCompanyName(companyName));
    }
}
