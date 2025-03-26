package com.ashmoday.loans.auth;

import com.ashmoday.loans.user.UserRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotNull(message = "User object is mandatory")
    @Valid
    private UserRequest user;

}
