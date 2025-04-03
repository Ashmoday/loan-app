package com.ashmoday.loans.loanPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Integer>, JpaSpecificationExecutor<LoanInstallment> {
}
