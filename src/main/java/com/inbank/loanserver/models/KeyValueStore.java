package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inbank.loanserver.utils.constraints.ValidKeyValueStore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * To store miscellaneous key-values
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
@ValidKeyValueStore
@EqualsAndHashCode(callSuper = true)
public final class KeyValueStore extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String key;

    private int value;

    @JsonProperty("isActive")
    private boolean isActive;
}
