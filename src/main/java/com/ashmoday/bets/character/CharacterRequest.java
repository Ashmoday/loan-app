package com.ashmoday.bets.character;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CharacterRequest {
    @NotNull
    private Integer id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;

}