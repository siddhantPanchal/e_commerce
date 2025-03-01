package io.siddhant.e_commerce.domain

import jakarta.persistence.*

@Table
@Entity(name = "role")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false, unique = true, length = 45)
    open var name: String? = null

    constructor(name: String?) {
        this.name = name
    }
}