package io.siddhant.e_commerce.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.siddhant.e_commerce.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key

@Service
class JwtService {


    @Value("\${security.jwt.secret_key}")
    private val secret: String = "";

    @Value("\${security.jwt.expiration_time}")
    private val jwtExpiration: Long = 0

    fun <T> extractClaims(token: String, resolver: (Claims) -> T): T {
        return resolver(getJwtParserBuilder().parseClaimsJws(token).body)
    }

    private fun getJwtParserBuilder(): JwtParser {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
    }

    fun createJwtToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.username)
            .setExpiration(java.util.Date(jwtExpiration))
            .signWith(getSignInKey())
            .compact()
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}