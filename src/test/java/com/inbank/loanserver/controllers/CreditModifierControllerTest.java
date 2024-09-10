package com.inbank.loanserver.controllers;

import com.inbank.loanserver.models.CreditModifier;
import com.inbank.loanserver.services.CreditModifierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for CreditModifierController
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class CreditModifierControllerTest {
    @Mock
    private CreditModifierService creditModifierService;

    @InjectMocks
    private CreditModifierController creditModifierController;

    private MockMvc mockMvc;

    private CreditModifier creditModifier;
    private UUID id;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(creditModifierController).build();
        id = UUID.randomUUID();
        creditModifier = new CreditModifier();
        creditModifier.setId(id);
        creditModifier.setName("Test Modifier");
        creditModifier.setValue(10);
        creditModifier.setActive(true);
    }

    @Test
    void testGetSortedCreditModifierByPage() throws Exception {
        Page<CreditModifier> page = new PageImpl<>(List.of(creditModifier));
        when(creditModifierService.findAllCreditModifiers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/credit-modifier")
                        .param("page", "0")
                        .param("items", "10")
                        .param("sort", "createdDate")
                        .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objList[0].id").value(id.toString()))
                .andExpect(jsonPath("$.objList[0].name").value("Test Modifier"))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetCreditModifierById() throws Exception {
        when(creditModifierService.findCreditModifierById(id)).thenReturn(creditModifier);

        mockMvc.perform(get("/credit-modifier/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Test Modifier"));
    }

    @Test
    void testDeleteCreditModifier() throws Exception {
        mockMvc.perform(get("/credit-modifier/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testRestoreCreditModifier() throws Exception {
        mockMvc.perform(get("/credit-modifier/restore/{id}", id))
                .andExpect(status().isOk());
    }
}