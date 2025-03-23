package com.ashmoday.bets.auth;

import com.ashmoday.bets.character.Character;
import com.ashmoday.bets.character.CharacterRepository;
import com.ashmoday.bets.role.Role;
import com.ashmoday.bets.role.RoleRepository;
import com.ashmoday.bets.security.JwtService;
import com.ashmoday.bets.user.User;
import com.ashmoday.bets.user.UserRepository;
import com.ashmoday.bets.user.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;


    @Value("${spring.security.oauth2.client.registration.custom-provider.client-id}")
    private String clientId;


    public void register(RegistrationRequest request) {
        UserRequest userRequest = request.getUser();

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        User user = User.builder()
                .username(userRequest.getUsername())
                .ucpId(userRequest.getId())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        List<Character> characters = Optional.ofNullable(userRequest.getCharacter())
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
