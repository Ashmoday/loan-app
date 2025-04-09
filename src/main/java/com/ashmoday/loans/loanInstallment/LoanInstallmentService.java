package com.ashmoday.loans.loanInstallment;

import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.loan.Loan;
import com.ashmoday.loans.loan.LoanRepository;
import com.ashmoday.loans.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanRepository loanRepository;
    private final InstallmentMapper installmentMapper;

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
        }

        loanInstallmentRepository.save(installment);
    }

    public int getDiscountValue(Integer loanId, Integer installmentId) {
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

    public void payEarly(Integer loanId, Integer installmentId, int paymentAmount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        LoanInstallment loanInstallment = loanInstallmentRepository.findById(installmentId)
                .orElseThrow(() -> new EntityNotFoundException("Installment not found"));

        boolean discountEligible = loanInstallment.getStatus() == InstallmentStatus.DISCOUNT_ELIGIBLE;

        if(!discountEligible) throw new OperationNotPermittedException("This installment is not eligible for discount");

        int discountedPayment = getDiscountValue(loanId, installmentId);

        if (paymentAmount < discountedPayment)
        {
            loanInstallment.setAmount(loanInstallment.getAmount() - paymentAmount);
            loanInstallmentRepository.save(loanInstallment);
            return;
        }
        loanInstallment.setAmount(0);
        loanInstallment.setStatus(InstallmentStatus.PAID);
        loanInstallmentRepository.save(loanInstallment);
    }

    public LoanInstallment getActualInstallment(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        LoanInstallment loanInstallment = loanInstallmentRepository.findByLoanAndStatusOrderByDueDateAsc(loan, InstallmentStatus.OVERDUE)
                .stream()
                .findFirst()
                .or(() -> loanInstallmentRepository.findByLoanAndStatusOrderByDueDateAsc(loan, InstallmentStatus.PENDING)
                        .stream()
                        .findFirst())
                .or(() -> loanInstallmentRepository.findByLoanAndStatusOrderByDueDateAsc(loan, InstallmentStatus.DISCOUNT_ELIGIBLE)
                        .stream()
                        .findFirst())
                .orElseThrow(() -> new EntityNotFoundException("No valid installment found"));

        return loanInstallment;
    }

    public int getInstallmentValue(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        LoanInstallment loanInstallment = getActualInstallment(loanId);

        return switch (loanInstallment.getStatus()) {
            case OVERDUE -> getOverDueFine(loanInstallment);
            case PENDING -> loanInstallment.getAmount();
            case DISCOUNT_ELIGIBLE -> getDiscountValue(loanId, loanInstallment.getId());
            default -> throw new OperationNotPermittedException("You cannot do this to this installment");
        };
    }

    public int getOverDueFine(LoanInstallment loanInstallment) {
        if (loanInstallment.getStatus() != InstallmentStatus.OVERDUE) {
            throw new OperationNotPermittedException("This installment is not overdue");
        }
        LocalDate dueDate = loanInstallment.getDueDate();
        LocalDate today = LocalDate.now();
        int overdueTime = (int) Math.max(0, ChronoUnit.DAYS.between(dueDate, today));

        double fineInterest = 0.01; // 1%
        int fixedFineValue = 500;
        int fineValue = (int) Math.ceil(loanInstallment.getAmount() * (fineInterest * overdueTime));
        int installmentOverduePrice = loanInstallment.getAmount() + fineValue + fixedFineValue;

        return installmentOverduePrice;
    }

    public List<LoanInstallmentResponse> getAllInstallments(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        List<LoanInstallmentResponse> loanInstallmentResponses = loanInstallmentRepository.findAllByLoan(loan)
                .stream()
                .map(installmentMapper::toLoanInstallmentResponse)
                .toList();

        return loanInstallmentResponses;
    }
}
