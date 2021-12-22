package com.raf.usermanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IService<User, Email> {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Email email);
    void delete(Email email);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
