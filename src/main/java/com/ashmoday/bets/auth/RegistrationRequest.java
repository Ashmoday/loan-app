package com.ashmoday.bets.auth;

import com.ashmoday.bets.character.CharacterRequest;
import com.ashmoday.bets.user.UserRequest;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotNull(message = "User object is mandatory")
    @Valid
    private UserRequest user;

}
