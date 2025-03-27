package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.character.CharacterRepository;
import com.ashmoday.loans.collateral.Collateral;
import com.ashmoday.loans.collateral.CollateralRepository;
import com.ashmoday.loans.exception.OperationNotPermittedException;
import com.ashmoday.loans.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.ashmoday.loans.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final CharacterRepository characterRepository;
    private final LoanMapper loanMapper;
    private final CollateralRepository collateralRepository;

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
}
