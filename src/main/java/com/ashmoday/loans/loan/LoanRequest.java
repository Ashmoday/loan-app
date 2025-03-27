package com.ashmoday.loans.loan;

import jakarta.persistence.*;
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
    private List<String> collaterals;

}
