package com.ashmoday.loans.loanInstallment;

import com.ashmoday.loans.character.CharacterResponse;
import com.ashmoday.loans.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("loan-payment")
@RequiredArgsConstructor
@Tag(name = "Installment Payment")
public class LoanInstallmentController {
    private final LoanInstallmentService service;

    @GetMapping("installment-value")
    public ResponseEntity<Integer> getInstalmentValue(
            Integer loanId
    ) {
        return ResponseEntity.ok(service.getInstallmentValue(loanId));
    }
    @GetMapping("installments")
    public ResponseEntity<List<LoanInstallmentResponse>> getAllInstalments(
            Integer loanId
    ) {
        return ResponseEntity.ok(service.getAllInstallments(loanId));
    }

    @PostMapping("pay-installment")
    public ResponseEntity<Integer> payInstallment(
            Integer loanInstallmentId,
            int amount
    ) {
        return ResponseEntity.ok(service.payInstallment(loanInstallmentId, amount));
    }
}
