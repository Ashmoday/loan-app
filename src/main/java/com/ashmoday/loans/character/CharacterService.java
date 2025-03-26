package com.ashmoday.loans.character;

import com.ashmoday.loans.common.PageResponse;
import com.ashmoday.loans.security.JwtService;
import com.ashmoday.loans.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;
    private final JwtService jwtService;

    public PageResponse<CharacterResponse> findAllCharacters(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Character> characters = characterRepository.findAllDisplayableCharacters(pageable, user.getId());
        List<CharacterResponse> characterResponse = characters.stream()
                .map(characterMapper::toCharacterResponse)
                .toList();
        return new PageResponse<>(
                characterResponse,
                characters.getNumber(),
                characters.getSize(),
                characters.getTotalElements(),
                characters.getTotalPages(),
                characters.isFirst(),
                characters.isLast()
        );
    }

   /*public AuthenticationResponse selectCharacter(Integer charId, Authentication connectedUser) {
        Character character = characterRepository.findById(charId)
                .orElseThrow(() -> new EntityNotFoundException("No character found with the ID:: " + charId));
        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(character.getUser().getId(), user.getId()))
        {
            throw new OperationNotPermittedException("You cannot choose someone else character");
        }

       HashMap<String, Object> claims = new HashMap<String, Object>();
       claims.put("ucpId", user.getUcpId());
       claims.put("charId", character.getId());



       String jwtToken = jwtService.generateToken(
               claims, character.getUser()
       );

       return AuthenticationResponse.builder()
               .token(jwtToken)
               .build();
    } */
}
