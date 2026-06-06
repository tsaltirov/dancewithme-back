package com.dance.me.auth.entity;

import java.time.LocalDateTime;

import com.dance.me.school.entity.School;
import com.dance.me.school.entity.SchoolRole;
import com.dance.me.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_school",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"user_id", "school_id"}
       ))
public class UserSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Enumerated(EnumType.STRING)
    @Column(name = "school_role", nullable = false, length = 30)
    private SchoolRole schoolRole;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
