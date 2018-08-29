package com.thorgil.cashbook.entrypoints.rest

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.validation.ConstraintViolationException

@ControllerAdvice
class RestResponseEntityExceptionHandler @Autowired constructor(var messageSource: MessageSource) {

    private val log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun processValidationError(ex: MethodArgumentNotValidException): ValidationErrorDTO {
        val result = ex.bindingResult
        val fieldErrors = result.fieldErrors

        log.info("Processing ${fieldErrors.size} field error(s)")

        val validationErrorDTO = processFieldErrors(fieldErrors)

        log.info(validationErrorDTO.toString())

        return validationErrorDTO
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun processRequestParameterValidationError(ex: ConstraintViolationException): ValidationErrorDTO {
        val constraintViolations = ex.constraintViolations

        log.info("Processing ${constraintViolations.size} constraint violation(s)")

        val  validationErrorDTO = ValidationErrorDTO()

        for (constraintViolation in constraintViolations) {
            // TODO finish
        }

        return validationErrorDTO
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ValidationErrorDTO {
        val dto = ValidationErrorDTO()

        for (fieldError in fieldErrors) {

            val fieldErrorCodes = fieldError.codes
            val fieldErrorCodeMessage = fieldErrorCodes!![0]

            dto.addFieldError(fieldError.field, fieldError.defaultMessage?: fieldErrorCodeMessage)
        }

        return dto
    }
}