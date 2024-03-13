package com.professul.professul.entity;

import com.professul.professul.util.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column
    private String email;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
