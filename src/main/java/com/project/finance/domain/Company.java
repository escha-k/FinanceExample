package com.project.finance.domain;

import com.project.finance.dto.CompanyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String ticker;

    public Company(CompanyDto companyDto) {
        this.name = companyDto.getName();
        this.ticker = companyDto.getTicker();
    }
}
