package com.ashmoday.loans.loanInstallment;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanInstallmentResponse {
    private Integer id;
    private Integer installmentNumber;
    private Integer amount;
    private LocalDate dueDate;
    private InstallmentStatus status;
}
