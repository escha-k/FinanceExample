package com.project.finance.scheduler;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.ScrapResult;
import com.project.finance.repository.CompanyRepository;
import com.project.finance.repository.DividendRepository;
import com.project.finance.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper scraper;

    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void scrapScheduling() {
        log.info("scrapping scheduler started");

        List<Company> companies = companyRepository.findAll();

        for (Company company : companies) {
            log.info("scrapping company: {}", company);

            CompanyDto companyDto = CompanyDto.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build();

            ScrapResult result = scraper.scrap(companyDto);

            result.getDividendList().stream()
                    .map(dto -> new Dividend(company, dto))
                    .forEach(entity -> {
                        if (!dividendRepository.existsByCompanyAndDate(company, entity.getDate())) {
                            dividendRepository.save(entity);
                        }
                    });

            // 연속 스크래핑으로 인한 과부하 방지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
