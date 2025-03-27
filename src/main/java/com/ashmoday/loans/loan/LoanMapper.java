package com.ashmoday.loans.loan;

import org.springframework.stereotype.Service;

@Service
public class LoanMapper {

  public Loan toLoan(LoanRequest request) {
        return Loan.builder()
                .amount(request.getAmount())
                .weeks(request.getWeeks())
                .build();
    }

}