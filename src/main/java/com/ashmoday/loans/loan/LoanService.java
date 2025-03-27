package com.ashmoday.loans.loan;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.character.CharacterRepository;
import com.ashmoday.loans.exception.OperationNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.ashmoday.loans.user.User;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final CharacterRepository characterRepository;
    private final LoanMapper loanMapper;

    public Loan requestLoan(LoanRequest request, Integer charId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Character character = characterRepository.findById(charId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        Boolean ownedCharacter = characterRepository.characterFromUserId(character, user.getId());

        if(!ownedCharacter) throw new OperationNotPermittedException("You don't own this character");

        boolean hasActiveLoan = !loanRepository.findByCharacterAndStatus(character, LoanStatus.ACTIVE).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.PENDING).isEmpty()
                || !loanRepository.findByCharacterAndStatus(character, LoanStatus.APPROVED).isEmpty();

        if (hasActiveLoan) throw new OperationNotPermittedException("You already have a loan");

        Loan loan = Loan.builder()
                .character(character)
                .amount(request.getAmount())
                .weeks(request.getWeeks())
                .interestRate(8.00)
                .status(LoanStatus.PENDING)
                .build();

        return loanRepository.save(loan);

    }
}
