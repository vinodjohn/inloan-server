package com.inbank.loanserver.handlers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals(
                "anonymousUser")) {
            return Optional.of(DEFAULT_AUDITOR);
        } else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(customUserDetails.getUsername());
        }
    }
}
