package com.thorgil.cashbook.entrypoints.rest.company.business.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.nhaarman.mockito_kotlin.whenever
import com.thorgil.cashbook.core.entity.BusinessTransactionType
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.company.GetCompany
import com.thorgil.cashbook.entrypoints.rest.business.transaction.AddBusinessTransactionPostBody
import com.thorgil.cashbook.entrypoints.rest.business.transaction.BusinessTransactionApiEndpoint
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDate
import java.time.Month

/**
 * One of the main purposes of integration testing with MockMvc is to verify that model objects are correctly populated
 * with form data.
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest
// TODO Use constructor-based val property for @MockBean when supported, see issue spring-boot#13113
class BusinessTransactionApiTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var company: GetCompany

    companion object {
        private  val companyProvider: GetCompany = object: GetCompany {
            override fun getCompany(): Company {
                return Company(entityName = "ABC Limited",
                        companyNumber = "123",
                        NZBN = "123",
                        incorporationDate = LocalDate.now(),
                        annualReturnFilingMonth = Month.JUNE,
                        gstStatus = GstStatus.REGISTERED)
            }
        }
        private val TRANSACTIONS_API__URL = BusinessTransactionApiEndpoint.BUSINESS_TRANSACTION_END_POINT_URL
    }

    @Test
    fun `post valid business transaction`() {

        // Arrange

        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.parse("2018-07-31"), amountInCents = 23000)

        val addBusinessTransactionJson = mapper.writeValueAsString(addBusinessTransactionPostBody)

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_API__URL).content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.validationErrorMessage", equalTo(null)))
                .andExpect(jsonPath("$.uuid", not(isEmptyOrNullString())))
    }

    @Test
    fun `post invalid business transaction - completed date in future`() {

        // Arrange

        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.now().plusDays(1), amountInCents = 23000)

        val addBusinessTransactionJson = mapper.writeValueAsString(addBusinessTransactionPostBody)

        // Act and Assert

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_API__URL).content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().string(
                        """
                            {"fieldErrors":[{"field":"completedDate","message":"Completed date must be a past or present date"}]}
                        """.trimIndent()
                ))
    }

    @Test
    fun `post invalid business transaction - scheduled date in past`() {

        // Arrange

        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                scheduledDate = LocalDate.now().minusDays(1), completedDate = null, amountInCents = 23000)

        val addBusinessTransactionJson = mapper.writeValueAsString(addBusinessTransactionPostBody)

        System.out.println("")
        System.out.println(addBusinessTransactionJson)
        System.out.println("")

        // Act and Assert

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_API__URL).content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().string(
                        """
                            {"fieldErrors":[{"field":"scheduledDate","message":"Scheduled date must be a future or present date"}]}
                        """.trimIndent()
                ))
    }

    @Test
    fun `post invalid business transaction - amountInCents negative`() {

        // Arrange

        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.parse("2018-07-31"), amountInCents = -23000)

        val addBusinessTransactionJson = mapper.writeValueAsString(addBusinessTransactionPostBody)

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_API__URL).content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().string(
                        """
                            {"fieldErrors":[{"field":"amountInCents","message":"must be greater than or equal to 0"}]}
                        """.trimIndent()
                ))
    }

    @Test
    fun `post invalid business transaction - gstInCents negative`() {

        // Arrange

        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.parse("2018-07-31"), amountInCents = 23000, gstInCents = -3450)

        val addBusinessTransactionJson = mapper.writeValueAsString(addBusinessTransactionPostBody)

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post(TRANSACTIONS_API__URL).content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.content().string(
                        """
                            {"fieldErrors":[{"field":"gstInCents","message":"must be greater than or equal to 0"}]}
                        """.trimIndent()
                ))
    }

}