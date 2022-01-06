package com.raf.usermanagement.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.raf.usermanagement.enums.Status;
import com.raf.usermanagement.models.Machine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByUserId(Long userId);
    // find all machines by user id and that are active
    List<Machine> findByUserIdAndActive(Long userId, boolean active);
    List<Machine> findByUserIdAndNameContainingAndStatusInAndCreatedAtBetween(Long userId, String name, List<Status> statuses, Date start, Date end);
    List<Machine> findByUserIdAndNameContaining(Long userId, String name);
    List<Machine> findByUserIdAndStatusIn(Long userId, List<Status> statuses);
    List<Machine> findByUserIdAndNameContainingAndStatusIn(Long userId, String name, List<Status> statuses);
    List<Machine> findByUserIdAndNameContainingAndCreatedAtBetween(Long userId, String name, Date start, Date end);
    List<Machine> findByUserIdAndStatusInAndCreatedAtBetween(Long userId, List<Status> statuses, Date start, Date end);
    List<Machine> findByUserIdAndCreatedAtBetween(Long userId, Date start, Date end);
    Optional<Machine> findByIdAndUserIdAndActive(Long id, Long userId, boolean active);
}
