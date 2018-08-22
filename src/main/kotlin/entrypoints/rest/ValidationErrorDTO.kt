package com.thorgil.cashbook.entrypoints.rest

data class FieldErrorDTO(val field: String, val message: String)

/**
 * NOTE: Do NOT make fields in this class private, if you do RestResponseEntityExceptionHandler will fail to
 * send a marshalled response (the client will see an empty JSON {}) message
 */
class ValidationErrorDTO {

    val fieldErrors = ArrayList<FieldErrorDTO>()

    fun addFieldError(path: String, message: String) {
        val error = FieldErrorDTO(path, message)
        fieldErrors.add(error)
    }

    override fun toString(): String {
        return "ValidationErrorDTO(fieldErrors=$fieldErrors)"
    }
}