package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Incoming loan requests
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
public class LoanRequest {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    private int loanAmount;
    private int loanPeriodMonths;
    private int creditScore;

    @Enumerated(EnumType.STRING)
    private LoanDecisionStatus loanDecisionStatus;

    private int loanDecisionMinAmount;
    private int loanDecisionMaxAmount;
    private int loanDecisionMinPeriod;
    private int loanDecisionMaxPeriod;
    private boolean isActive;
}
