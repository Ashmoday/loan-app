package com.ashmoday.loans.collateral;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CollateralRequest {
    @NotNull
    private String description;
    @NotNull
    private String ownershipProof;
    @NotNull
    private Integer estimatedValue;
    @NotNull
    private CollateralType type;
}