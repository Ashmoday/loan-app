package com.ashmoday.bets.auth;

import com.ashmoday.bets.character.CharacterRequest;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "Username is mandatory")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotEmpty(message = "Ucp is mandatory")
    @NotBlank(message = "Ucp is mandatory")
    private Integer ucpId;

    @Valid
    private List<CharacterRequest> characters;

}
