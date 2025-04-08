package com.ashmoday.loans.loanInstallment;

import com.ashmoday.loans.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Integer>, JpaSpecificationExecutor<LoanInstallment> {
    List<LoanInstallment> findByLoanAndStatus(Loan loan, InstallmentStatus installmentStatus);
    List<LoanInstallment> findByLoanAndStatusOrderByDueDateAsc(Loan loan, InstallmentStatus installmentStatus);
}
