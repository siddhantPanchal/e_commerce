package io.siddhant.e_commerce.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Table
@Entity(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    open var id: Long? = null

    @JsonIgnore
    @Column(name = "password", nullable = false)
    open var password: String? = null

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "roles_id")
    open var role: Role? = null

    @Column(name = "username", nullable = false, unique = true, length = 45)
    open var username: String? = null


    @Column(name = "token", unique = true)
    open var token: String? = null

    constructor(username: String, password: String, role: Role, token: String? = null) {
        this.password = password
        this.role = role
        this.username = username
        this.token = token
    }
}