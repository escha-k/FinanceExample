package com.project.finance.scraper;

import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.ScrapResult;

public interface Scraper {

    CompanyDto scrapCompanyByTicker(String ticker);

    ScrapResult scrap(CompanyDto company);
}
