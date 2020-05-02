package com.photoarchive.repositories;

import com.photoarchive.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
    Optional<Token> findByUser_Email(String email);
    boolean existsByValue(String value);
}
