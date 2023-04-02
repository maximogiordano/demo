package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"firstName", "lastName"}))
public class Person {
    public static final int FIRST_NAME_LENGTH = 30;
    public static final int LAST_NAME_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = FIRST_NAME_LENGTH, nullable = false)
    private String firstName;

    @Column(length = LAST_NAME_LENGTH, nullable = false)
    private String lastName;
}
