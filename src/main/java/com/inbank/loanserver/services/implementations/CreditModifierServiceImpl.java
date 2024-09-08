package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.CreditModifierNotFoundException;
import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.repositories.CreditModifierRepository;
import com.inbank.loanserver.services.CreditModifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of CreditModifier Service
 *
 * @author vinodjohn
 * @created 30.08.2024
 */
@Service
@Transactional
public class CreditModifierServiceImpl implements CreditModifierService {
    @Autowired
    private CreditModifierRepository creditModifierRepository;

    @Override
    public CreditModifier createCreditModifier(CreditModifier creditModifier) {
        String name = creditModifier.getName().trim().toUpperCase();
        creditModifier.setName(name);
        creditModifier.setActive(true);
        return creditModifierRepository.saveAndFlush(creditModifier);
    }

    @Override
    public CreditModifier findCreditModifierById(UUID id) throws CreditModifierNotFoundException {
        Optional<CreditModifier> optionalCreditModifier = creditModifierRepository.findById(id);

        if (optionalCreditModifier.isEmpty()) {
            throw new CreditModifierNotFoundException(id);
        }

        return optionalCreditModifier.get();
    }

    @Override
    public CreditModifier findCreditModifierByName(String name) throws CreditModifierNotFoundException {
        Optional<CreditModifier> optionalCreditModifier =
                creditModifierRepository.findByNameAndIsActiveTrue(name.toUpperCase());

        if (optionalCreditModifier.isEmpty()) {
            throw new CreditModifierNotFoundException(name);
        }

        return optionalCreditModifier.get();
    }

    @Override
    public CreditModifier findRandomCreditModifier() throws CreditModifierNotFoundException {
        Optional<CreditModifier> optionalCreditModifier = creditModifierRepository.findRandom();

        if (optionalCreditModifier.isEmpty()) {
            throw new CreditModifierNotFoundException("No Random found!");
        }

        return optionalCreditModifier.get();
    }

    @Override
    public Page<CreditModifier> findAllCreditModifiers(Pageable pageable) {
        return creditModifierRepository.findAll(pageable);
    }

    @Override
    public CreditModifier updateCreditModifier(CreditModifier creditModifier) throws CreditModifierNotFoundException {
        if (findCreditModifierById(creditModifier.getId()) != null) {
            String name = creditModifier.getName().trim().toUpperCase();
            creditModifier.setName(name);
            return creditModifierRepository.saveAndFlush(creditModifier);
        }

        return null;
    }

    @Override
    public void deleteCreditModifierById(UUID id) throws CreditModifierNotFoundException {
        CreditModifier creditModifier = findCreditModifierById(id);
        creditModifier.setActive(false);
        creditModifierRepository.saveAndFlush(creditModifier);
    }

    @Override
    public void restoreCreditModifierById(UUID id) throws CreditModifierNotFoundException {
        CreditModifier creditModifier = findCreditModifierById(id);
        creditModifier.setActive(true);
        creditModifierRepository.saveAndFlush(creditModifier);
    }
}
