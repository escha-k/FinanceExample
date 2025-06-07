package com.project.finance.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DividendDto {

    private LocalDate date;
    private String dividend;
}
