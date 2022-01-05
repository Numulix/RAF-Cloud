package com.raf.usermanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.raf.usermanagement.models.Machine;
import com.raf.usermanagement.models.User;
import com.raf.usermanagement.repositories.MachineRepository;
import com.raf.usermanagement.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MachineService {
    
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository, UserService userService) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Get all machines from currently logged in user
    public List<Machine> getAllUserMachines() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            User u = user.get();
            return machineRepository.findByUserId(u.getId());
        }
        return new ArrayList<Machine>();
    }

}
