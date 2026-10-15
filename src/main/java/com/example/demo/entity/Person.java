package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "person")
@Data
public class Person {

  public static final int MAX_NAME_LENGTH = 30;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", length = MAX_NAME_LENGTH, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = MAX_NAME_LENGTH, nullable = false)
  private String lastName;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

}
