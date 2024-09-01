package com.inbank.loanserver.models;

import com.inbank.loanserver.utils.constraints.ValidCreditModifier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Credit modifier model
 *
 * @author vinodjohn
 * @created 29.08.2024
 */

@Data
@Entity
@ValidCreditModifier
@EqualsAndHashCode(callSuper = true)
public final class CreditModifier extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private int value;

    private boolean isActive;
}
