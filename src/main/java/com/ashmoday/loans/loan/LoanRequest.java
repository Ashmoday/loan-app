package com.ashmoday.loans.loan;

import com.ashmoday.loans.collateral.Collateral;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LoanRequest {
    @NotNull
    private Integer charId;
    @NotNull
    private Integer amount;
    @NotNull
    private Integer weeks;

    @DecimalMin(value = "1.0")
    private Double interestRate;

    private List<Collateral> collaterals;

}
