package com.ashmoday.loans.loanPayment;

import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.loan.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;

    public void payInstallment(Integer loanInstallmentId, int amount)
    {
        LoanInstallment installment = loanInstallmentRepository.findById(loanInstallmentId)
                .orElseThrow(() -> new EntityNotFoundException("Installment not found"));

        if (installment.getStatus() == InstallmentStatus.PAID) {
            throw new IllegalArgumentException("Installment is already paid.");
        }
        if (amount <= 0 ) throw new OperationNotPermittedException("Invalid amount");
        installment.setAmount(installment.getAmount() - amount);

        if (installment.getAmount() <= 0) {
            installment.setStatus(InstallmentStatus.PAID);
        } else {
            installment.setStatus(InstallmentStatus.PARTIALLY_PAID);
        }

        loanInstallmentRepository.save(installment);

    }
}
