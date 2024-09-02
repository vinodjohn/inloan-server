package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.UUID;

/**
 * Refresh token model
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class TokenRefresh extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant endTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Person person;

    @JsonProperty("isActive")
    private boolean isActive;
}
