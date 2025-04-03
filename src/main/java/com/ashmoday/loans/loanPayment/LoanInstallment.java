package com.ashmoday.loans.loanPayment;

import com.ashmoday.loans.common.BaseEntity;
import com.ashmoday.loans.loan.Loan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoanInstallment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private Integer installmentNumber;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private InstallmentStatus status = InstallmentStatus.PENDING;
}
