package com.raf.usermanagement.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import com.raf.usermanagement.enums.Status;
import com.raf.usermanagement.models.Machine;
import com.raf.usermanagement.models.User;
import com.raf.usermanagement.repositories.ErrorMessageRepository;
import com.raf.usermanagement.repositories.MachineRepository;
import com.raf.usermanagement.repositories.UserRepository;
import com.raf.usermanagement.tasks.RestartMachineTask;
import com.raf.usermanagement.tasks.StartMachineTask;
import com.raf.usermanagement.tasks.StopMachineTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MachineService {

    private final TaskScheduler taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
    
    private final MachineRepository machineRepository;
    private final UserService userService;
    private final ErrorMessageRepository errorMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserService userService, ErrorMessageRepository errorMessageRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userService = userService;
        this.errorMessageRepository = errorMessageRepository;
        this.userRepository = userRepository;
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

    // Find machine by id
    public Optional<Machine> findById(Long id) {
        return machineRepository.findById(id);
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
            machine.setCreatedAt(new Date());
            machine.setOperationActive(false);
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

    public List<Machine> searchMachines(String name, List<Status> statusList, Date dateFrom, Date dateTo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            User u = user.get();

            // return an empty list if either one of the dates is not null and the other is null
            if (dateFrom != null && dateTo == null || dateFrom == null && dateTo != null) {
                return new ArrayList<Machine>();
            }

            // Search by all parameters
            if (name != null && statusList != null && dateFrom != null && dateTo != null) {
                return machineRepository.findByUserIdAndNameContainingAndStatusInAndCreatedAtBetween(u.getId(), name, statusList, dateFrom, dateTo);
            }

            // If all parameters except name are null, search by name only
            if (name != null && statusList == null && dateFrom == null && dateTo == null) {
                return machineRepository.findByUserIdAndNameContaining(u.getId(), name);
            }

            // If all parameters except status are null, search by status only
            if (name == null && statusList != null && dateFrom == null && dateTo == null) {
                return machineRepository.findByUserIdAndStatusIn(u.getId(), statusList);
            }

            // name and status not null
            if (name != null && statusList != null && dateFrom == null && dateTo == null) {
                return machineRepository.findByUserIdAndNameContainingAndStatusIn(u.getId(), name, statusList);
            }

            // name and date not null
            if (name != null && statusList == null && dateFrom != null && dateTo != null) {
                return machineRepository.findByUserIdAndNameContainingAndCreatedAtBetween(u.getId(), name, dateFrom, dateTo);
            }
            
            // status and date not null
            if (name == null && statusList != null && dateFrom != null && dateTo != null) {
                return machineRepository.findByUserIdAndStatusInAndCreatedAtBetween(u.getId(), statusList, dateFrom, dateTo);
            }

            // dates only
            if (name == null && statusList == null && dateFrom != null && dateTo != null) {
                return machineRepository.findByUserIdAndCreatedAtBetween(u.getId(), dateFrom, dateTo);
            }

            // if all parameters are null, just search all user machines
            if (name == null && statusList == null && dateFrom == null && dateTo == null) {
                return machineRepository.findByUserId(u.getId());
            }
        }
        return new ArrayList<Machine>();
    }

    @Async
    public void startMachine(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();

            if (m.getStatus() == Status.RUNNING) {
                return;
            }

            m.setOperationActive(true);
            machineRepository.save(m);
            try {
                Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                m.setStatus(Status.RUNNING);
                m.setOperationActive(false);
                machineRepository.save(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Async
    public void stopMachine(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();

            if (m.getStatus() == Status.STOPPED) {
                return;
            }

            m.setOperationActive(true);
            machineRepository.save(m);
            try {
                Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                m.setStatus(Status.STOPPED);
                m.setOperationActive(false);
                machineRepository.save(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Async
    public void restartMachine(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();

            if (m.getStatus() == Status.STOPPED) {
                return;
            }

            m.setOperationActive(true);
            machineRepository.save(m);
            try {
                Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                m.setStatus(Status.STOPPED);
                machineRepository.save(m);
                Thread.sleep((long) (Math.random() * (15 - 10) + 10) * 1000);
                m.setStatus(Status.RUNNING);
                m.setOperationActive(false);
                machineRepository.save(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // function to check that if a machine can be started
    // a machine can be started only if the status is STOPPED
    public boolean canStartMachine(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();
            if (m.getStatus() == Status.STOPPED) {
                return true;
            }
        }
        return false;
    }

    // check if a machine can be stopped
    // a machine can be stopped only if the status is RUNNING
    public boolean canStopMachine(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();
            if (m.getStatus() == Status.RUNNING) {
                return true;
            }
        }
        return false;
    }

    // function to check if a machine is busy
    // a machine is busy if the operationActive flag is true
    public boolean isBusy(Long id) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            Machine m = machine.get();
            if (m.isOperationActive()) {
                return true;
            }
        }
        return false;
    }

    public void scheduleMachineStop(Long id, LocalDateTime dateTime) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            // get logged in user
            Optional<User> u = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            User user = u.get();

            // get the machine
            Machine m = machine.get();
            Date scheduleDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            
            taskScheduler.schedule(
                new StopMachineTask(m.getId(), user.getId(), machineRepository, errorMessageRepository, userService),
                scheduleDate);
        }
    }

    public void scheduleMachineStart(Long id, LocalDateTime dateTime) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            // get logged in user
            Optional<User> u = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            User user = u.get();

            // get the machine
            Machine m = machine.get();
            Date scheduleDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            
            taskScheduler.schedule(
                new StartMachineTask(m.getId(), user.getId(), machineRepository, errorMessageRepository, userService),
                scheduleDate);
        }
    }

    public void scheduleMachineRestart(Long id, LocalDateTime dateTime) {
        Optional<Machine> machine = machineRepository.findById(id);
        if (machine.isPresent()) {
            // get logged in user
            Optional<User> u = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            User user = u.get();

            // get the machine
            Machine m = machine.get();
            Date scheduleDate = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            
            taskScheduler.schedule(
                new RestartMachineTask(m.getId(), user.getId(), machineRepository, errorMessageRepository, userService),
                scheduleDate);
        }
    }

}
