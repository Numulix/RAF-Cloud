package com.raf.usermanagement.repositories;

import java.util.List;

import com.raf.usermanagement.models.ErrorMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    // return a list of error messages for a given user id
    List<ErrorMessage> findByUserId(Long userId);
}
