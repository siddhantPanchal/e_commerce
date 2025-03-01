package io.siddhant.e_commerce.service

import io.jsonwebtoken.ExpiredJwtException
import io.siddhant.e_commerce.domain.Role
import io.siddhant.e_commerce.domain.User
import io.siddhant.e_commerce.repository.RoleRepository
import io.siddhant.e_commerce.repository.UserRepository
import io.siddhant.e_commerce.util.JwtService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService,
    private val roleRepository: RoleRepository
) {


    fun getUser(username: String): User {
        return userRepository.findByUsernameIgnoreCase(username)
            ?: throw UsernameNotFoundException("User with username $username not found")
    }

    fun login(username: String, password: String): User {
        val user = getUser(username);
        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("Invalid password")
        }
        try {
            if (user.token == null || jwtService.extractClaims(
                    user.token!!,
                    { it.expiration.before(java.util.Date()) })
            ) {
                user.token = jwtService.createJwtToken(user)
                userRepository.save(user)
            }
        } catch (e: ExpiredJwtException) {
            user.token = jwtService.createJwtToken(user)
            userRepository.save(user)
        }
//        TODO: return profile
        return user
    }

    fun signup(username: String, password: String, name: String): User {

        if (userRepository.findByUsernameIgnoreCase(username) != null) {
            throw Exception("User with username $username already exists")
        }

        val userRole = roleRepository.findByName("ROLE_USER") ?: roleRepository.save(Role("ROLE_USER"));
        val user = User(username, passwordEncoder.encode(password), userRole);
//       TODO: create profile
        user.token = jwtService.createJwtToken(user);
        return userRepository.save(user)
    }

}