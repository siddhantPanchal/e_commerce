package io.siddhant.e_commerce.configurations

import io.siddhant.e_commerce.repository.UserRepository
import io.siddhant.e_commerce.util.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver


@EnableWebSecurity
@Configuration
class SecurityConfiguration(
    private val userRepository: UserRepository,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val jwtService: JwtService
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/auth/**", "/error/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/seller/**").hasRole("SELLER")
                    .anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .logout(Customizer.withDefaults())
            .authenticationProvider(getAuthenticationProvider())
            .addFilterBefore(object : OncePerRequestFilter() {

                override fun shouldNotFilter(request: HttpServletRequest): Boolean {
                    println(request.servletPath)
                    println(request.servletPath.startsWith("/auth/"))
                    return request.servletPath.startsWith("/auth/")
                }

                override fun doFilterInternal(
                    request: HttpServletRequest,
                    response: HttpServletResponse,
                    filterChain: FilterChain
                ) {
                    try {
                        println("filtering request path: ${request.servletPath}")
                        val authHeader = request.getHeader("Authorization")
                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            val jwt = authHeader.substring(7)
                            val claimsJws = jwtService.extractClaims(jwt) { it }
                            if (!claimsJws.expiration.before(java.util.Date())) {
                                throw UsernameNotFoundException("Token expired")
                            }
                            val username = claimsJws.subject
                            val user = userRepository.findByUsernameIgnoreCase(username)
                                ?: throw UsernameNotFoundException("User with username $username not found")
                            val principle = UserPrinciple(user)
                            val authentication = UsernamePasswordAuthenticationToken(
                                principle,
                                null,
                                principle.authorities,
                            )
                            request.setAttribute("user", user)
                            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authentication
                        }
                        filterChain.doFilter(request, response)
                    } catch (e: Exception) {
                        handlerExceptionResolver.resolveException(request, response, null, e);
                    }
                }

            }, UsernamePasswordAuthenticationFilter::class.java)

            .build();
    }


    @Bean
    fun getAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(getUserDetailsService())
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun getUserDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            val user = userRepository.findByUsernameIgnoreCase(username)
                ?: throw UsernameNotFoundException("User with username $username not found")
            UserPrinciple(user);
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = listOf("http://localhost:8080")
//        configuration.allowedMethods = listOf("GET", "POST")
        configuration.allowedHeaders = listOf("Authorization", "Content-Type")

        val source = UrlBasedCorsConfigurationSource()

        source.registerCorsConfiguration("/**", configuration)

        return source
    }

}