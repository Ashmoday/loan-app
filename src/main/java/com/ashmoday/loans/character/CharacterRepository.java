package com.ashmoday.loans.character;

import com.ashmoday.loans.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CharacterRepository extends JpaRepository<Character, Integer>, JpaSpecificationExecutor<User> {
    Boolean existsByCharId(Integer charId);
    /*Character findByIdAndUcpId(Integer id, Integer ucpId);
    List<Character> findAllByUser(Integer ucpId);*/

    @Query("""
            SELECT character FROM Character character
            WHERE character.user.id = :userId
            """)
    Page<Character> findAllDisplayableCharacters(Pageable pageable, Integer userId);
    @Query("""
        SELECT c FROM Character c
        WHERE c.user.id = :userId
        """)
    List<Character> findAllByUserId(Integer userId);
}
