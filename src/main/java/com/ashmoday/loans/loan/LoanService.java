package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.character.CharacterRepository;
import com.ashmoday.loans.collateral.Collateral;
import com.ashmoday.loans.collateral.CollateralRepository;
import com.ashmoday.loans.common.PageResponse;
import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.loanInstallment.InstallmentStatus;
import com.ashmoday.loans.loanInstallment.LoanInstallment;
import com.ashmoday.loans.loanInstallment.LoanInstallmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.ashmoday.loans.user.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final CharacterRepository characterRepository;
    private final LoanMapper loanMapper;
    private final CollateralRepository collateralRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    public Loan requestLoan(LoanRequest request, Integer charId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Character character = characterRepository.findById(charId)
                .orElseThrow(() -> new EntityNotFoundException("Character not found"));

        Boolean ownedCharacter = characterRepository.characterFromUserId(character, user.getId());

        if(!ownedCharacter) throw new OperationNotPermittedException("You don't own this character");

        boolean hasActiveLoan = !loanRepository.findByCharacterAndStatus(character, LoanStatus.ACTIVE).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.PENDING).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.WAITING_APPROVAL).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.APPROVED).isEmpty();

        if (hasActiveLoan) throw new OperationNotPermittedException("You already have a loan");

        Loan loan = Loan.builder()
                .character(character)
                .amount(request.getAmount())
                .weeks(request.getWeeks())
                .interestRate(8.00)
                .status(LoanStatus.PENDING)
                .build();
        loanRepository.save(loan);

        if (request.getCollaterals() != null && !request.getCollaterals().isEmpty()) {
            List<Collateral> collaterals = request.getCollaterals().stream()
                    .map(c -> Collateral.builder()
                            .loan(loan)
                            .description(c.getDescription())
                            .ownershipProof(c.getOwnershipProof())
                            .estimatedValue(c.getEstimatedValue())
                            .type(c.getType())
                            .build())
                    .collect(Collectors.toList());

            collateralRepository.saveAll(collaterals);
        }

        return loan;

    }

    public Loan manageLoan(Integer loanId, LoanRequest loanRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (!isAdmin) throw new OperationNotPermittedException("You can't do that");
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("No loan found with the ID:: " + loanId));

        loan.setAmount(loanRequest.getAmount());
        loan.setInterestRate(loanRequest.getInterestRate());
        loan.setWeeks(loanRequest.getWeeks());
        loan.setStatus(LoanStatus.WAITING_APPROVAL);
        return loanRepository.save(loan);
    }

    public PageResponse<LoanResponse> findAllPendingLoans(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Loan> loans = loanRepository.findByStatus(LoanStatus.PENDING, pageable);
        List<LoanResponse> loanResponse = loans.stream()
                .map(loanMapper::toLoanResponse)
                .toList();

        return new PageResponse<>(
                loanResponse,
                loans.getNumber(),
                loans.getSize(),
                loans.getTotalElements(),
                loans.getTotalPages(),
                loans.isFirst(),
                loans.isLast()
        );

    }

    public Integer acceptLoan(Integer loanId, Integer charId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Character character = characterRepository.findById(charId)
                .orElseThrow(() -> new EntityNotFoundException("Character not found"));
        boolean ownedCharacter = characterRepository.characterFromUserId(character, user.getId());

        if(!ownedCharacter) throw new OperationNotPermittedException("You don't own this character");

        Loan loan = loanRepository.findByIdAndCharacter(loanId, character)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found for this character"));

        boolean hasActiveLoan = !loanRepository.findByCharacterAndStatus(character, LoanStatus.ACTIVE).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.PENDING).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.APPROVED).isEmpty()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.REJECTED).isPresent();

        if (hasActiveLoan) throw new OperationNotPermittedException("You can't accept a loan while you have another one active");

        loan.setStatus(LoanStatus.APPROVED);
        return loan.getId();
    }

    public Integer rejectLoan(Integer loanId, Integer charId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        Character character = characterRepository.findById(charId)
                .orElseThrow(() -> new EntityNotFoundException("Character not found"));
        boolean ownedCharacter = characterRepository.characterFromUserId(character, user.getId()) || isAdmin;

        if(!ownedCharacter) throw new OperationNotPermittedException("You don't own this character");

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found for this id:: " + loanId));

        if(!Objects.equals(loan.getCharacter().getId(), character.getId()) && !isAdmin)
        {
            throw new OperationNotPermittedException("You cannot reject someone else loan");
        }

        boolean hasActiveLoan = loanRepository.findByIdAndStatus(loanId, LoanStatus.ACTIVE).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.APPROVED).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.REJECTED).isPresent();

        if (hasActiveLoan) throw new OperationNotPermittedException("You can't reject an active loan");

        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
        return loan.getId();
    }

    public Integer activateLoan(Integer loanId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if(!isAdmin) throw new OperationNotPermittedException("You don't have access to this");

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found for this id:: " + loanId));

        boolean hasActiveLoan = loanRepository.findByIdAndStatus(loanId, LoanStatus.ACTIVE).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.WAITING_APPROVAL).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.PENDING).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.COMPLETED).isPresent()
                || loanRepository.findByIdAndStatus(loanId, LoanStatus.REJECTED).isPresent();

        if (hasActiveLoan) throw new OperationNotPermittedException("You can't activate this loan");
        loan.setStatus(LoanStatus.ACTIVE);
        loanRepository.save(loan);

        LocalDate dueDate = getNextSaturday();
        int weeklyPayment = getWeeklyPayment(loan.getAmount(), loan.getWeeks(), loan.getInterestRate());
        List<LoanInstallment> installments = new ArrayList<>();

        for(int i = 0; i < loan.getWeeks(); i++)
        {
            InstallmentStatus status = (i == 0) ? InstallmentStatus.PENDING : InstallmentStatus.DISCOUNT_ELIGIBLE;

            LoanInstallment loanInstallment = LoanInstallment.builder()
                    .loan(loan)
                    .installmentNumber(i+1)
                    .amount(weeklyPayment)
                    .dueDate(dueDate.plusWeeks(i))
                    .status(status)
                    .build();

            installments.add(loanInstallment);
        }
        loanInstallmentRepository.saveAll(installments);
        return loan.getId();
    }


    public LocalDate getNextSaturday() {
        LocalDate today = LocalDate.now();
        LocalDate nextSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        long daysUntilSaturday = ChronoUnit.DAYS.between(today, nextSaturday);

        if (daysUntilSaturday < 5) {
            return nextSaturday.plusWeeks(1);
        }
        return nextSaturday;
    }

    public Integer getWeeklyPayment(int amount, int weeks, double interestRate)
    {
        return (amount / weeks) + (int)(amount * interestRate / 100);
    }

}
