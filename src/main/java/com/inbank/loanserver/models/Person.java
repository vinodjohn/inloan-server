package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Person model
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
public class Person {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    private String personalIdCode;

    @ManyToOne(fetch = FetchType.EAGER)
    private CreditModifier creditModifier;

    private boolean isActive;
}
