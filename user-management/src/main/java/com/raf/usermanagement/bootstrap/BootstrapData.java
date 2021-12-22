package com.raf.usermanagement.bootstrap;

import com.raf.usermanagement.models.Permission;
import com.raf.usermanagement.models.User;
import com.raf.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading data...");

        // Pravljenje korisnika sa svim permisijama
        User adminUser = new User();
        adminUser.setEmail("admin@mail.com");
        adminUser.setName("Pera");
        adminUser.setSurname("Peric");
        adminUser.setPassword(this.passwordEncoder.encode("admin"));

        Permission allPerms = new Permission();
        allPerms.setCanCreateUser(1);
        allPerms.setCanReadUser(1);
        allPerms.setCanUpdateUser(1);
        allPerms.setCanDeleteUser(1);

        adminUser.setPermission(allPerms);

        userRepository.save(adminUser);

        for (int i = 0; i <= 20; i++) {
            User user = new User();
            user.setEmail("user."+(i+1)+"@mail.com");
            user.setName("User " + (i + 1));
            user.setSurname("Useric");
            user.setPassword(this.passwordEncoder.encode("sifra" + (i+1)));

            Permission permission = new Permission();
            permission.setCanCreateUser(random.nextBoolean() ? 1 : 0);
            permission.setCanDeleteUser(random.nextBoolean() ? 1 : 0);
            permission.setCanReadUser(random.nextBoolean() ? 1 : 0);
            permission.setCanUpdateUser(random.nextBoolean() ? 1 : 0);

            user.setPermission(permission);

            userRepository.save(user);
        }
        System.out.println("All users created");
    }
}
