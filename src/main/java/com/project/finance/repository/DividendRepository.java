package com.project.finance.repository;

import com.project.finance.domain.Company;
import com.project.finance.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Integer> {

    List<Dividend> findAllByCompany(Company company);
}
