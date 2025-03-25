package com.ashmoday.bets.character;

import org.springframework.stereotype.Service;

@Service
public class CharacterMapper {
    public CharacterResponse toCharacterResponse(Character request)
    {
        return CharacterResponse.builder()
                .id(request.getId())
                .charId(request.getCharId())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phoneNumber(request.getPhoneNumber())
                .bankRouting(request.getBankRouting())
                .balance(request.getBalance())
                .build();
    }
}
