package io.siddhant.e_commerce.configurations

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApplicationResponse(private val status: HttpStatus, private val message: String, private val data: Any?) {

    
    companion object {
        fun success(message: String, data: Any?, status: HttpStatus = HttpStatus.OK): ApplicationResponse {
            return ApplicationResponse(status, message, data)
        }

        fun error(message: String): ApplicationResponse {
            return ApplicationResponse(HttpStatus.BAD_REQUEST, message, null)
        }
    }

    fun build(): ResponseEntity<Map<String, Any?>> {
        return ResponseEntity(mapOf("message" to message, "data" to data, "status" to status.value()), status);
    }

}