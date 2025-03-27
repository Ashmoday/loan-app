package com.ashmoday.loans.collateral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollateralRepository extends JpaRepository<Collateral, Integer>, JpaSpecificationExecutor<Collateral> {
}
