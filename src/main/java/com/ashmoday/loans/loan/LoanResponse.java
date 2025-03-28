package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.collateral.CollateralResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponse {
    private Integer id;
    private Character character;
    private Integer amount;
    private Integer weeks;
    private Double interestRate;
    private LoanStatus status;
    private List<CollateralResponse> collaterals;

}
