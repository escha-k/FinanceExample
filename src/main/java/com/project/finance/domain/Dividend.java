package com.project.finance.domain;

import com.project.finance.dto.DividendDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Company company;

    private LocalDate date;
    private String dividend;

    public Dividend(Company company, DividendDto dividendDto) {
        this.company = company;
        this.date = dividendDto.getDate();
        this.dividend = dividendDto.getDividend();
    }
}
