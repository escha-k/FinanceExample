package com.project.finance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {

    String ticker;
    String name;
}
