package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.Person;
import com.inbank.loanserver.models.TokenRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Refresh Token related data queries
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Repository
public interface TokenRefreshRepository extends PagingAndSortingRepository<TokenRefresh, UUID>,
        JpaRepository<TokenRefresh, UUID> {
    Optional<TokenRefresh> findByToken(String token);

    @Query(value = "SELECT * FROM token_refresh tr WHERE tr.person_id = :personId AND tr.is_active = true ORDER BY tr" +
            ".created_date DESC LIMIT 1", nativeQuery = true)
    Optional<TokenRefresh> findLatestActiveTokenByPerson(@Param("personId") UUID personId);
}
