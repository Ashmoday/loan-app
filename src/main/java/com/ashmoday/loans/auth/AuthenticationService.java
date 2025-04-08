package com.ashmoday.loans.auth;

import com.ashmoday.loans.character.Character;
import com.ashmoday.loans.character.CharacterRepository;
import com.ashmoday.loans.role.Role;
import com.ashmoday.loans.role.RoleRepository;
import com.ashmoday.loans.security.JwtService;
import com.ashmoday.loans.user.User;
import com.ashmoday.loans.user.UserRepository;
import com.ashmoday.loans.user.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



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
    private final PasswordEncoder passwordEncoder;


    public void register(RegistrationRequest request) {
        UserRequest userRequest = request.getUser();

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));

        User user = User.builder()
                .username(userRequest.getUsername())
                .ucpId(userRequest.getId())
                .password(passwordEncoder.encode(userRequest.getPassword()))
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

    public AuthenticationResponse login(AuthenticationRequest authRequest)
    {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        User user = (User) auth.getPrincipal();
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("ucpId", user.getUcpId());


        String jwtToken = jwtService.generateToken(
                claims, user
        );

        List<Character> characters = characterRepository.findAllByUserId(user.getId());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
