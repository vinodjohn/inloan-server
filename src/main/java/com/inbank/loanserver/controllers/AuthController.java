package com.inbank.loanserver.controllers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.dtos.PersonDto;
import com.inbank.loanserver.dtos.SignIn;
import com.inbank.loanserver.dtos.SignUp;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.TokenRefresh;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.TokenRefreshService;
import com.inbank.loanserver.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * Controller to handle security related requests
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PersonService personService;

    @Autowired
    private TokenRefreshService tokenRefreshService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) throws PersonNotFoundException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signIn.personalIdCode(), signIn.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        ResponseCookie generatedJwtCookie = securityUtils.generateJwtCookie(customUserDetails.getUsername());
        TokenRefresh tokenRefresh = tokenRefreshService.createRefreshToken(customUserDetails.getPerson().getId());
        ResponseCookie refreshJwtCookie = securityUtils.generateRefreshJwtCookie(tokenRefresh.getToken());

        String role = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No authorities found for person"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, generatedJwtCookie.getValue());
        httpHeaders.add(HttpHeaders.SET_COOKIE, refreshJwtCookie.getValue());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new PersonDto(customUserDetails.getPerson().getId(),
                        customUserDetails.getPerson().getPersonalIdCode(),
                        MessageFormat.format("{0} {1}", customUserDetails.getPerson().getFirstName(),
                                customUserDetails.getPerson().getLastName()), role));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp) throws RoleNotFoundException,
            CreditModifierNotFoundException {
        Person person = new Person();
        person.setFirstName(signUp.firstName());
        person.setLastName(signUp.lastName());
        person.setPersonalIdCode(signUp.personalIdCode());
        person.setPassword(signUp.password());

        personService.createPerson(person);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest) {
        String tokenRefreshRetrieved = securityUtils.getJwtRefreshFromCookies(httpServletRequest);

        if (tokenRefreshRetrieved != null && !tokenRefreshRetrieved.isBlank()) {
            TokenRefresh tokenRefresh = tokenRefreshService.findTokenRefreshByToken(tokenRefreshRetrieved);
            TokenRefresh tokenRefreshVerified = tokenRefreshService.verifyTokenExpiry(tokenRefresh);
            ResponseCookie refreshJwtCookie =
                    securityUtils.generateJwtCookie(tokenRefreshVerified.getPerson().getPersonalIdCode());
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshJwtCookie.getValue())
                    .build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut() {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID personId = customUserDetails.getPerson().getId();
        tokenRefreshService.deleteTokenRefreshByPersonId(personId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, securityUtils.getCleanJwtCookie().getValue());
        httpHeaders.add(HttpHeaders.SET_COOKIE, securityUtils.getCleanJwtRefreshCookie().getValue());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }
}
