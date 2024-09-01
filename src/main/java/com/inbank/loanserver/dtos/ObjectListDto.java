package com.inbank.loanserver.dtos;

import java.util.List;

/**
 * Person List DTO
 *
 * @author vinodjohn
 * @created 01.09.2024
 */
public record ObjectListDto(List<?> objList, int currentPage, long totalElements) {
}
