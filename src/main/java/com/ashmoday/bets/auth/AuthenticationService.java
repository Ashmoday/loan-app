package com.ashmoday.bets.auth;

import com.ashmoday.bets.character.Character;
import com.ashmoday.bets.character.CharacterRepository;
import com.ashmoday.bets.role.RoleRepository;
import com.ashmoday.bets.user.User;
import com.ashmoday.bets.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;

    public void register(RegistrationRequest request) {

        if (userRepository.findByUcpId(request.getUcpId()).isPresent())
        {
            throw new IllegalStateException("User already has an account");
        }
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        User user = User.builder()
                .username(request.getUsername())
                .ucpId(request.getUcpId())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        List<Character> characters = Optional.ofNullable(request.getCharacters())
                .orElse(Collections.emptyList())
                .stream()
                .filter(characterRequest -> !characterRepository.existsByCharId(characterRequest.getId()))
                .map(characterRequest -> Character.builder()
                        .charId(characterRequest.getId())
                        .firstname(characterRequest.getFirstname())
                        .lastname(characterRequest.getLastname())
                        .user(user)
                        .balance(0)
                        .build()
                ).collect(Collectors.toList());
        if (!characters.isEmpty())
        {
            characterRepository.saveAll(characters);
        }
    }
}
