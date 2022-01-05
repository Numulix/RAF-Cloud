package com.raf.usermanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.raf.usermanagement.enums.Status;
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
    private final UserService userService;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserService userService) {
        this.machineRepository = machineRepository;
        this.userService = userService;
    }

    // Get all machines from currently logged in user
    public List<Machine> getAllUserMachines() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            User u = user.get();
            return machineRepository.findByUserIdAndActive(u.getId(), true);
        }
        return new ArrayList<Machine>();
    }

    public Machine createMachine(String name) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            User u = user.get();
            Machine machine = new Machine();
            machine.setUser(u);
            machine.setStatus(Status.STOPPED);
            machine.setActive(true);
            machine.setName(name);
            return machineRepository.save(machine);
        }
        return null;
    }

    // function that sets the active flag of a machine to false
    public void deleteMachine(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            User u = user.get();
            Optional<Machine> machine = machineRepository.findById(id);
            if (machine.isPresent()) {
                Machine m = machine.get();
                if (m.getUser().getId().equals(u.getId())) {
                    m.setActive(false);
                    machineRepository.save(m);
                }
            }
        }
    }

}
