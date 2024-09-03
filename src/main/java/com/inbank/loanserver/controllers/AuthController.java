package com.inbank.loanserver.controllers;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.dtos.*;
import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.TokenRefresh;
import com.inbank.loanserver.services.PersonService;
import com.inbank.loanserver.services.TokenRefreshService;
import com.inbank.loanserver.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String jwt = securityUtils.generateJwtToken(customUserDetails);

        String role = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No authorities found for person"));

        TokenRefresh tokenRefresh = tokenRefreshService.createRefreshToken(customUserDetails.getPerson().getId());

        return ResponseEntity.ok(new TokenResponse(customUserDetails.getPerson().getId(),
                customUserDetails.getUsername(), role, jwt, tokenRefresh.getToken(), null));
    }

    @PostMapping("/signup")
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
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenRefresh tokenRefresh = tokenRefreshService.findTokenRefreshByToken(tokenRefreshRequest.refreshToken());
        TokenRefresh tokenRefreshVerified = tokenRefreshService.verifyTokenExpiry(tokenRefresh);
        String token = securityUtils.generateTokenFromUsername(tokenRefreshVerified.getPerson().getPersonalIdCode());

        return ResponseEntity.ok(new TokenRefreshResponse(token, tokenRefreshRequest.refreshToken(), null));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut() {
        CustomUserDetails customUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID personId = customUserDetails.getPerson().getId();
        tokenRefreshService.deleteTokenRefreshByPersonId(personId);
        return ResponseEntity.ok().build();
    }
}
