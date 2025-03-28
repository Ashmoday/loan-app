package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Integer>, JpaSpecificationExecutor<Loan> {
    List<Loan> findByCharacterAndStatus(Character character, LoanStatus status);
    List<Loan> findByCharacter(Character character);
    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);
    Optional<Loan> findByIdAndCharacter(Integer loanId, Character character);
    Optional<Loan> findByIdAndStatus(Integer loanId, LoanStatus status);
}
