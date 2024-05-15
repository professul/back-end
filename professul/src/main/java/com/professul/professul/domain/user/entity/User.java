package com.professul.professul.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;

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


    // 생성자 정의
    public User(Long userId,String email, String name, String password, Status status, Timestamp createDate, Timestamp editDate, UserRole role) {
        this.userId=userId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.status = status;
        this.createDate = createDate;
        this.editDate = editDate;
        this.role = role;
    }

    public void changeName(String newName){
        this.name=newName;
        this.editDate=new Timestamp(System.currentTimeMillis());
    }

    //비밀번호 변경 메서드
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder){
        this.password=passwordEncoder.encode(newPassword);
    }

    public void suspend(){
        this.status=Status.SUSPENDED;
    }

    public void ban(){
        this.status=Status.BANNED;
    }

    public void withdraw(){
        this.status=Status.CANCELED;
    }
}
