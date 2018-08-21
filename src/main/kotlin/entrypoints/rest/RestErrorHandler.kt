package com.thorgil.cashbook.entrypoints.rest

import com.thorgil.cashbook.entrypoints.restu.ValidationErrorDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class RestErrorHandler(@Autowired private val messageSource: MessageSource) {

    private val log = LoggerFactory.getLogger(RestErrorHandler::class.java)

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

    private fun processFieldErrors(fieldErrors: List<FieldError>): ValidationErrorDTO {
        val dto = ValidationErrorDTO()

        for (fieldError in fieldErrors) {

            log.info("${fieldError.field}")

            val localizedErrorMessage = resolveLocalizedErrorMessage(fieldError)

            log.info("${localizedErrorMessage}")

            dto.addFieldError(fieldError.field, localizedErrorMessage)
        }

        return dto
    }

    private fun resolveLocalizedErrorMessage(fieldError: FieldError): String {
        val currentLocale = LocaleContextHolder.getLocale()
        var localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale)

        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage == fieldError.defaultMessage) {
            val fieldErrorCodes = fieldError.codes
            localizedErrorMessage = fieldErrorCodes!![0]
        }

        return localizedErrorMessage
    }
}