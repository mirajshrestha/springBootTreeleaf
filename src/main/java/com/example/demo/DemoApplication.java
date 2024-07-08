package com.example.demo;

import com.example.demo.Model.Role;
import com.example.demo.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(RoleRepository roleRepository) {
        return (args) -> {
            Role adminRole = new Role(1L, "ROLE_ADMIN");
            Role userRole = new Role(2L, "ROLE_USER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

        };
    }
}
