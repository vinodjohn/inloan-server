package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.exceptions.TokenRefreshException;
import com.inbank.loanserver.models.TokenRefresh;
import com.inbank.loanserver.repositories.TokenRefreshRepository;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.TokenRefreshService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of TokenRefreshService
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Service
@Transactional
public class TokenRefreshServiceImpl implements TokenRefreshService {
    @Autowired
    private TokenRefreshRepository tokenRefreshRepository;

    @Autowired
    private PersonService personService;

    @Value("${inLoan.app.jwtRefreshExpirationMs}")
    private Long tokenRefreshDurationMs;

    @Override
    public TokenRefresh createRefreshToken(UUID personId) throws PersonNotFoundException {
        TokenRefresh tokenRefresh = new TokenRefresh();
        tokenRefresh.setToken(UUID.randomUUID().toString());
        tokenRefresh.setEndTime(Instant.now().plusMillis(tokenRefreshDurationMs));
        tokenRefresh.setPerson(personService.findPersonById(personId));
        tokenRefresh.setActive(true);

        return tokenRefreshRepository.saveAndFlush(tokenRefresh);
    }

    @Override
    public TokenRefresh findTokenRefreshById(UUID id) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findById(id);

        if (tokenRefresh.isEmpty()) {
            throw new TokenRefreshException(id);
        }

        return tokenRefresh.get();
    }

    @Override
    public TokenRefresh findTokenRefreshByToken(String token) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findByToken(token);

        if (tokenRefresh.isEmpty()) {
            throw new TokenRefreshException(token);
        }

        return tokenRefresh.get();
    }

    @Override
    public TokenRefresh verifyTokenExpiry(TokenRefresh tokenRefresh) {
        var now = Instant.now();

        if (tokenRefresh.getEndTime().isBefore(now)) {
            deleteTokenRefreshById(tokenRefresh.getId());
            throw new TokenRefreshException(tokenRefresh.getToken(), "Refresh token was expired. Please sign in again");
        }

        return tokenRefresh;
    }

    @Override
    public void deleteTokenRefreshById(UUID id) throws TokenRefreshException {
        TokenRefresh tokenRefresh = findTokenRefreshById(id);
        tokenRefresh.setActive(false);
        tokenRefreshRepository.saveAndFlush(tokenRefresh);
    }

    @Override
    public TokenRefresh deleteTokenRefreshByPersonId(UUID personId) {
        Optional<TokenRefresh> tokenRefresh = tokenRefreshRepository.findLatestActiveTokenByPerson(personId);

        if (tokenRefresh.isEmpty()) {
            throw new RuntimeException(MessageFormat.format("No active token refresh found for person id {0}",
                    personId));
        }

        return tokenRefresh.get();
    }
}
