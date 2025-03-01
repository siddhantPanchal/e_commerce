package io.siddhant.e_commerce.controller

import io.siddhant.e_commerce.configurations.ApplicationResponse
import io.siddhant.e_commerce.service.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val userService: UserService) {

    @RequestMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Map<String, Any?>> {
        return try {
            ApplicationResponse.success(
                "Login successful",
                userService.login(loginRequest.username!!, loginRequest.password!!)
            ).build()
        } catch (e: Exception) {
            e.printStackTrace();
            ApplicationResponse.error(e.message!!).build()
        }
    }

    @RequestMapping("/signup")
    fun signup(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<Map<String, Any?>> {
        return try {
            ApplicationResponse.success(
                "Signup successful",
                userService.signup(signUpRequest.username!!, signUpRequest.password!!, signUpRequest.name!!)
            ).build()
        } catch (e: Exception) {
            ApplicationResponse.error(e.message!!).build()
        }
    }

    class LoginRequest(
        @field:NotBlank() @field:NotNull() @field:Email() val username: String?,
        @field:NotBlank() @field:NotNull() val password: String?
    )

    class SignUpRequest(
        @field:NotBlank() @field:Email() @field:NotNull() val username: String?,
        @field:NotBlank() @field:NotNull() val password: String?,
        @field:NotBlank() @field:NotNull(message = "Name cannot be empty") val name: String?
    )


}