package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.KeyValueStoreNotFoundException;
import com.inbank.loanserver.models.KeyValueStore;
import com.inbank.loanserver.services.KeyValueStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;
import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Controller to handle key-value store related operations
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
@RestController
@RequestMapping("/kv-store")
public class KeyValueStoreController {
    @Autowired
    private KeyValueStoreService keyValueStoreService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedKeyValueStoreByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<KeyValueStore> keyValueStorePage = keyValueStoreService.findAllKeyValueStores(PageRequest.of(pageNum,
                totalItem, getSortOfColumn(sort, order)));
        List<KeyValueStore> keyValueStoreList = keyValueStorePage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(keyValueStoreList, pageNum,
                keyValueStorePage.getTotalElements());
        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKeyValueStoreById(@PathVariable UUID id) throws KeyValueStoreNotFoundException {
        KeyValueStore keyValueStore = keyValueStoreService.findKeyValueStoreById(id);
        return ResponseEntity.ok(keyValueStore);
    }

    @PostMapping
    public ResponseEntity<?> createKeyValueStore(@RequestBody KeyValueStore keyValueStore) {
        KeyValueStore newKeyValueStore = keyValueStoreService.createKeyValueStore(keyValueStore);
        return new ResponseEntity<>(newKeyValueStore, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateKeyValueStore(@RequestBody KeyValueStore keyValueStore) throws KeyValueStoreNotFoundException {
        KeyValueStore updatedKeyValueStore = keyValueStoreService.updateKeyValueStore(keyValueStore);
        return ResponseEntity.ok(updatedKeyValueStore);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteKeyValueStore(@PathVariable UUID id) throws KeyValueStoreNotFoundException {
        keyValueStoreService.deleteKeyValueStoreById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreKeyValueStore(@PathVariable UUID id) throws KeyValueStoreNotFoundException {
        keyValueStoreService.restoreKeyValueStoreById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
