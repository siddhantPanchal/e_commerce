package io.siddhant.e_commerce.configurations

import io.siddhant.e_commerce.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional

open class UserPrinciple(private val user: User) : UserDetails {
    

    @Transactional
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role!!}".uppercase()));
    }

    override fun getPassword(): String {
        return user.password!!;
    }

    override fun getUsername(): String {
        return user.username!!
    }
}