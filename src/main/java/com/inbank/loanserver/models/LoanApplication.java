package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Incoming loan requests and its decisions
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class LoanApplication extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    private int requestAmount;
    private int requestPeriod;

    @JsonProperty("isActive")
    private boolean isActive;
}
