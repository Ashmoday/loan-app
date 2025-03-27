package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

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
}
