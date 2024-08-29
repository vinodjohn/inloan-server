package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * To store miscellaneous key-values
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
public class KeyValueStore {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String key;
    private int value;
    private boolean isActive;
}
