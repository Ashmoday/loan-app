package com.ashmoday.loans.collateral;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollateralResponse {
    private String description;
    private String ownershipProof;
    private Integer estimatedValue;
    private CollateralType type;
}