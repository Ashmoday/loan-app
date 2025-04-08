package com.ashmoday.loans.loan;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("loan")
@RequiredArgsConstructor
@Tag(name = "Loans")
public class LoanController {
    private final LoanService service;

    @PostMapping("request-loan")
    public ResponseEntity<Loan> requestLoan(
            @Valid @RequestBody LoanRequest request,
            Integer charId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(service.requestLoan(request, charId, authentication));
    }

    @PostMapping("manage-loan")
    public ResponseEntity<Loan> manageLoan(
            Integer loanId,
            @Valid @RequestBody LoanRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.manageLoan(loanId, request, connectedUser));
    }
}
