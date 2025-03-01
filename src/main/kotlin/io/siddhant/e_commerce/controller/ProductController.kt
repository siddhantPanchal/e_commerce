package io.siddhant.e_commerce.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController {

    @GetMapping
    fun getProducts(): String {
        return "products";
    }


}