package com.project.finance.repository;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Integer> {

    boolean existsByCompanyAndDate(Company company, LocalDate date);

    List<Dividend> findAllByCompany(Company company);

    @Transactional
    void deleteAllByCompany(Company company);
}
