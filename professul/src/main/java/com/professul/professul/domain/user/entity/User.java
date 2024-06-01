package com.professul.professul.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;
@Slf4j
@Entity
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp editDate;

    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.ROLE_USER;

    private LocalDate suspendUntil;

    private String banReason;


    // 생성자 정의
    public User(Long userId,String email, String name, String password, Status status, UserRole role) {
        this.userId=userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public void changeName(String newName){
        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name=newName;
        this.editDate=new Timestamp(System.currentTimeMillis());
        log.info("User name changed to {}", newName);

    }

    //비밀번호 변경 메서드
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder){
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }
        this.password=passwordEncoder.encode(newPassword);
    }

    public void suspend(LocalDate until){
        if (until == null || until.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Suspend date must be in the future");
        }
        if (this.status != Status.ACTIVE) {
            throw new IllegalStateException("Only active users can be suspended");
        }
        this.status=Status.SUSPENDED;
        this.suspendUntil=until;
    }

    public void ban(String reason){
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Ban reason cannot be null or empty");
        }
        this.status=Status.BANNED;
        this.banReason=reason;
    }

    public void withdraw() {
        if(this.status!=Status.CANCELED){
            throw new IllegalStateException("이미 탈퇴한 회원입니다");
        }
        this.status=Status.CANCELED;
        log.info("탈퇴");
    }

    public void activate() {
        if (this.status != Status.SUSPENDED) {
            throw new IllegalStateException("Only suspended users can be activated");
        }
        this.status = Status.ACTIVE;
        this.suspendUntil = null;
        log.info("User activated");
    }
}
