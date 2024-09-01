package com.inbank.loanserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inbank.loanserver.utils.constraints.ValidPerson;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Person model
 *
 * @author vinodjohn
 * @created 29.08.2024
 */
@Data
@Entity
@ValidPerson
@EqualsAndHashCode(callSuper = true)
public final class Person extends Auditable<String> {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String personalIdCode;

    private String password;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private CreditModifier creditModifier;

    @JsonProperty("isActive")
    private boolean isActive;
}
