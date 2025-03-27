package com.ashmoday.loans.character;

import com.ashmoday.loans.auth.AuthenticationResponse;
import com.ashmoday.loans.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService service;

    @GetMapping
    public ResponseEntity<PageResponse<CharacterResponse>> findAllCharacters(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllCharacters(page, size, connectedUser));
    }

    @PostMapping("/select/{charId}")
    public ResponseEntity<AuthenticationResponse> selectCharacter(
            @PathVariable Integer charId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.selectCharacter(charId, connectedUser));
    }

}
