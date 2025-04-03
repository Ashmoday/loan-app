package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.collateral.Collateral;
import com.ashmoday.loans.common.BaseEntity;
import com.ashmoday.loans.loanPayment.LoanInstallment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Loan extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="character_id", nullable = false)
    private Character character;
    @Column(nullable = false)
    private Integer amount;
    @Column(nullable = false)
    private Integer weeks;
    @Column(nullable = false)
    private Double interestRate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING;

    @OneToMany(mappedBy = "loan")
    List<Collateral> collaterals;
    @OneToMany(mappedBy = "loan")
    List<LoanInstallment> loanInstallments;


}
