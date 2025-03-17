package com.ashmoday.bets.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true, name = "ucp_id", nullable = false)
    private Integer ucpId;
    @Column(unique = true, nullable = false)
    private String username;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "user")
    private List<Character> characters;
}
