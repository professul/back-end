package com.professul.professul.entity;

import com.professul.professul.util.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    @Column(unique = true)
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.ROLE_USER;

//    public User(String email, String name, String password, UserRole role) {
//        this.email = email;
//        this.name = name;
//        this.password = password;
//        this.role = role;
//    }
//
//    protected User(){
//
//    }

}
