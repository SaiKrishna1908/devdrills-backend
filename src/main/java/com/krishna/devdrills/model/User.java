package com.krishna.devdrills.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;
}
