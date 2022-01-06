package com.raf.usermanagement.repositories;

import com.raf.usermanagement.models.ErrorMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    
}
