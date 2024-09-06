package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Loan Contract model
 *
 * @author vinodjohn
 * @created 05.09.2024
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class LoanContract extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private LoanOffer loanOffer;

    private float interestRate;

    private int period;

    private BigDecimal monthlyAmount;

    @JsonProperty("isActive")
    private boolean isActive;
}
