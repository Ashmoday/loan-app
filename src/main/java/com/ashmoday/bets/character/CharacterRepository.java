package com.ashmoday.bets.character;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Boolean existsByCharId(Integer charId);
}
