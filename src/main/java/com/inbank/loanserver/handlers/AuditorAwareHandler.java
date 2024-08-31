package com.inbank.loanserver.handlers;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

import java.util.Optional;

import static com.inbank.loanserver.utils.Constants.Audit.DEFAULT_AUDITOR;

/**
 * Custom handler to implement AuditorAware
 *
 * @author vinodjohn
 * @created 31.08.2024
 */
public class AuditorAwareHandler implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(DEFAULT_AUDITOR);
    }
}
