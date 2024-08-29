package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Incoming loan requests and its decisions
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
public class LoanBook {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    private int requestAmount;
    private int requestPeriod;
    private int creditScore;

    @Enumerated(EnumType.STRING)
    private LoanDecisionStatus loanDecisionStatus;

    private int decisionMinAmount;
    private int decisionMaxAmount;
    private int decisionMinPeriod;
    private int decisionMaxPeriod;
    private boolean isActive;
}
