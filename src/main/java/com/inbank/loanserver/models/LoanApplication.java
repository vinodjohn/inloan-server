package com.inbank.loanserver.models;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    private int requestAmount;
    private int requestPeriod;

    private boolean isActive;
}
