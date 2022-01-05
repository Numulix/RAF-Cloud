package com.raf.usermanagement.controllers;

import java.util.Optional;

import com.raf.usermanagement.models.User;
import com.raf.usermanagement.services.MachineService;
import com.raf.usermanagement.services.UserService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/machines")
public class MachineController {

    private final UserService userService;
    private final MachineService machineService;

    public MachineController(UserService userService, MachineService machineService) {
        this.userService = userService;
        this.machineService = machineService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUserMachines() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);

        if (user.isPresent()) {
            User u = user.get();
            if (u.getPermission().getCanSearchMachine() == 1) {
                return ResponseEntity.ok(machineService.getAllUserMachines());
            } else {
                return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.status(401).build();
    }

}
