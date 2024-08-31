package com.inbank.loanserver.configurations;

import com.inbank.loanserver.handlers.AuditorAwareHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for Auditing
 *
 * @author vinodjohn
 * @created 31.08.2024
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareHandler();
    }
}