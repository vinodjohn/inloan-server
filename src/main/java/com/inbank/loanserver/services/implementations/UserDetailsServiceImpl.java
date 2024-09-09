package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.configurations.security.CustomUserDetails;
import com.inbank.loanserver.exceptions.PersonNotFoundException;
import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserDetailsService
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Person person = personService.findActivePersonByPersonalIdCode(username);
            return new CustomUserDetails(person);
        } catch (PersonNotFoundException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage());
        }
    }
}

