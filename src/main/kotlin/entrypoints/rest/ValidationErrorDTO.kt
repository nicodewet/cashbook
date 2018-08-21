package com.thorgil.cashbook.entrypoints.restu

data class FieldErrorDTO(val field: String, val message: String)

class ValidationErrorDTO {

    private val fieldErrors = ArrayList<FieldErrorDTO>()

    fun addFieldError(path: String, message: String) {
        val error = FieldErrorDTO(path, message)
        fieldErrors.add(error)
    }

    override fun toString(): String {
        return "ValidationErrorDTO(fieldErrors=$fieldErrors)"
    }
}