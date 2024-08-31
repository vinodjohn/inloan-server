package com.inbank.loanserver.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Loan offer model
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class LoanOffer extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanApplication loanApplication;

    private float creditScore;
    private int loanAmount;
    private int minPeriod;
    private int maxPeriod;

    @Enumerated(EnumType.STRING)
    private LoanOfferStatus loanOfferStatus;

    private boolean isActive;
}
