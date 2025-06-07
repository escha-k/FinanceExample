package com.project.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapResult {

    private CompanyDto company;
    private List<DividendDto> dividendList;

    public ScrapResult() {
        this.dividendList = new ArrayList<>();
    }
}
