package com.ashmoday.loans.loan;

import com.ashmoday.loans.collateral.CollateralResponse;
import org.springframework.stereotype.Service;

@Service
public class LoanMapper {

  public Loan toLoan(LoanRequest request) {
        return Loan.builder()
                .amount(request.getAmount())
                .weeks(request.getWeeks())
                .build();
    }

    public LoanResponse toLoanResponse(Loan loan) {

      return LoanResponse.builder()
              .id(loan.getId())
              .amount(loan.getAmount())
              .weeks(loan.getWeeks())
              .interestRate(loan.getInterestRate())
              .status(loan.getStatus())
              .collaterals(loan.getCollaterals().stream()
                      .map(collateral -> new CollateralResponse(
                              collateral.getDescription(),
                              collateral.getOwnershipProof(),
                              collateral.getEstimatedValue(),
                              collateral.getType()
                      ))
                      .toList())
              .build();
    }

}