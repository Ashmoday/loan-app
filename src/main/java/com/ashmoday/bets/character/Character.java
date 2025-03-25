package com.ashmoday.bets.character;


import com.ashmoday.bets.common.BaseEntity;
import com.ashmoday.bets.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Character extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Integer charId;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    private String phoneNumber;
    private String bankRouting;
    @Column(nullable = false)
    private Integer balance = 0;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @Transient
    public String getFullname()
    {
        return this.firstname + " " + this.lastname;
    }
}
