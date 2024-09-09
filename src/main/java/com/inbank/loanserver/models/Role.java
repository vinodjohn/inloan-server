package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Role model
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class Role extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @JsonProperty("isActive")
    private boolean isActive;
}
