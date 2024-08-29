package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

/**
 * Credit modifier model
 *
 * @author vinodjohn
 * @created 29.08.2024
 */

@Data
@Entity
public class CreditModifier {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private int value;
    private boolean isActive;
}
