package com.project.finance.scraper;

import com.project.finance.constants.Month;
import com.project.finance.dto.CompanyDto;
import com.project.finance.dto.DividendDto;
import com.project.finance.dto.ScrapResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String DIVIDEND_HISTORY_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d&filter=div";
    public static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s/";
    private static final long START_TIME = 86400L;

    @Override
    public ScrapResult scrap(CompanyDto company) {
        ScrapResult scrapResult = new ScrapResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;
            String url = String.format(DIVIDEND_HISTORY_URL, company.getTicker(), START_TIME, now);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Element table = document.getElementsByClass("table-container yf-1jecxey").get(0);
            Element tbody = table.selectFirst("tbody");

            List<DividendDto> list = new ArrayList<>();
            for (Element tr : tbody.select("tr")) {
                String txt = tr.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToInt(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend =splits[3];

                if (month < 0) {
                    throw new IllegalArgumentException("Invalid month: " + month);
                }

                DividendDto dividendDto = DividendDto.builder()
                        .date(LocalDate.of(year, month, day))
                        .dividend(dividend)
                        .build();
                list.add(dividendDto);
            }

            scrapResult.setDividendList(list);
        } catch (IOException | NullPointerException e) {
            // TODO : 스크랩 실패 시 예외 처리
            throw new RuntimeException(e);
        }

        return scrapResult;
    }

    @Override
    public CompanyDto scrapCompanyByTicker(String ticker) {
        try {
            String url = String.format(SUMMARY_URL, ticker);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Element title = document.getElementsByClass("yf-xxbei9").get(0);
            String companyName = title.text().split("\\(")[0].strip();

            return CompanyDto.builder().ticker(ticker).name(companyName).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}