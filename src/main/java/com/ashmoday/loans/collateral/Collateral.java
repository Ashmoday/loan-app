package com.ashmoday.loans.collateral;

import com.ashmoday.loans.common.BaseEntity;
import com.ashmoday.loans.loan.Loan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Collateral extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String ownershipProof;
    @Column(nullable = false)
    private Integer estimatedValue;
    @Enumerated(EnumType.STRING)
    private CollateralType type;

}
