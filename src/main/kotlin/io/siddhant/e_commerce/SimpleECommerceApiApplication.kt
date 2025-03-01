package io.siddhant.e_commerce

import io.siddhant.e_commerce.domain.Role
import io.siddhant.e_commerce.domain.User
import io.siddhant.e_commerce.repository.RoleRepository
import io.siddhant.e_commerce.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class SimpleECommerceApiApplication(
    val roleRepository: RoleRepository,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) {

    //    @Bean
    fun start(): CommandLineRunner {
        return CommandLineRunner {
            val adminRole = roleRepository.findByName("ROLE_ADMIN") ?: roleRepository.save(Role("ROLE_ADMIN"));
            val userRole = roleRepository.findByName("ROLE_USER") ?: roleRepository.save(Role("ROLE_USER"));


            var adminUser = userRepository.findByUsernameIgnoreCase("admin")
            if (adminUser != null) {
                userRepository.delete(adminUser)
            }
            adminUser = userRepository.save(
                User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    adminRole
                )
            );

            var user = userRepository.findByUsernameIgnoreCase("user")
            if (user != null) {
                userRepository.delete(user)
            }
            user = userRepository.save(
                User(
                    "user",
                    passwordEncoder.encode("user"),
                    userRole
                )
            );

        }
    }
}

fun main(args: Array<String>) {
    runApplication<SimpleECommerceApiApplication>(*args)
}
