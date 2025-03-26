package com.ashmoday.loans.character;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CharacterResponse {

    private Integer id;
    private Integer charId;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String bankRouting;
    private Integer balance;
}
