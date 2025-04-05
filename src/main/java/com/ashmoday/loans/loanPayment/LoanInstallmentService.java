package com.ashmoday.loans.loanPayment;

import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.loan.Loan;
import com.ashmoday.loans.loan.LoanRepository;
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

    public void payEarly(Integer loanId, int paymentAmount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        List<LoanInstallment> futureInstallments = loanInstallmentRepository
                .findByLoanAndStatus(loan, InstallmentStatus.DISCOUNT_ELIGIBLE);

        int discountRate = (int)(loan.getAmount() * loan.getInterestRate()) / 2;
        double discountedPayment = paymentAmount * (1 - discountRate);

        for (LoanInstallment installment : futureInstallments) {
            if (discountedPayment <= 0) break;

            double toPay = Math.min(installment.getRemainingAmount(), discountedPayment);
            installment.setRemainingAmount(installment.getRemainingAmount() - toPay);

            if (installment.getRemainingAmount() <= 0) {
                installment.setStatus(InstallmentStatus.PAID);
            }

            installmentRepository.save(installment);
            discountedPayment -= toPay;
        }
    }
}
