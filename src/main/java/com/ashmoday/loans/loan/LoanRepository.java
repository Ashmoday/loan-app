package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer>, JpaSpecificationExecutor<Loan> {
    List<Loan> findByCharacterAndStatus(Character character, LoanStatus status);
    List<Loan> findByCharacter(Character character);
}
