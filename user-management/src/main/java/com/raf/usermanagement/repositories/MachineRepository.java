package com.raf.usermanagement.repositories;

import java.util.List;

import com.raf.usermanagement.models.Machine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByUserId(Long userId);
}
