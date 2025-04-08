package com.ashmoday.loans.loanInstallment;

import org.springframework.stereotype.Service;

@Service
public class InstallmentMapper {

    public LoanInstallmentResponse toLoanInstallmentResponse(LoanInstallment loanInstallment)
    {
        return LoanInstallmentResponse.builder()
                .id(loanInstallment.getId())
                .installmentNumber(loanInstallment.getInstallmentNumber())
                .dueDate(loanInstallment.getDueDate())
                .amount(loanInstallment.getAmount())
                .status(loanInstallment.getStatus())
                .build();
    }
}
