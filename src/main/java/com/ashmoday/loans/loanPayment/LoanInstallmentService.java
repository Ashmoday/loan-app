package com.ashmoday.loans.loanPayment;

import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.loan.Loan;
import com.ashmoday.loans.loan.LoanRepository;
import com.ashmoday.loans.loan.LoanStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanRepository loanRepository;
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

    public int getEarlyPayment(Integer loanId, Integer installmentId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        LoanInstallment loanInstallment = loanInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> new EntityNotFoundException("Installment not found"));

        boolean discountEligible = loanInstallment.getStatus() == InstallmentStatus.DISCOUNT_ELIGIBLE;

        if(!discountEligible) throw new OperationNotPermittedException("This installment is not eligible for discount");

        int discountRate = (int)(loan.getAmount() * loan.getInterestRate() / 100);
        int discountedPayment = loanInstallment.getAmount() - (discountRate / 2);

        return discountedPayment;
    }
}
