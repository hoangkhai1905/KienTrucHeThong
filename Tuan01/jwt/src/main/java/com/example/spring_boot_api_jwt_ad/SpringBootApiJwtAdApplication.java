package com.example.spring_boot_api_jwt_ad;

import com.example.spring_boot_api_jwt_ad.entity.Role;
import com.example.spring_boot_api_jwt_ad.entity.User;
import com.example.spring_boot_api_jwt_ad.repository.RoleRepository;
import com.example.spring_boot_api_jwt_ad.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootApiJwtAdApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootApiJwtAdApplication.class, args);
    }

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role adminRole = roleRepository.findByRoleName("ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setRoleName("ADMIN");
                role.setRoleKey("ROLE_ADMIN");
                return roleRepository.save(role);
            });

            Role guestRole = roleRepository.findByRoleName("GUEST").orElseGet(() -> {
                Role role = new Role();
                role.setRoleName("GUEST");
                role.setRoleKey("ROLE_GUEST");
                return roleRepository.save(role);
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
                admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("guest").isEmpty()) {
                User guest = new User();
                guest.setUsername("guest");
                guest.setPassword(new BCryptPasswordEncoder().encode("guest123"));
                guest.setRoles(new HashSet<>(Collections.singletonList(guestRole)));
                userRepository.save(guest);
            }
        };
    }

}
