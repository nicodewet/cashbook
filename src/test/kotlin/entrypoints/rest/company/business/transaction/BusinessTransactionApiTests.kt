package com.thorgil.cashbook.entrypoints.rest.company.business.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.whenever
import com.thorgil.cashbook.core.entity.BusinessTransactionType
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.company.GetCompany
import com.thorgil.cashbook.entrypoints.rest.business.transaction.AddBusinessTransactionPostBody
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

    @Test
    fun `post valid business transaction`() {

        // Arrange

        val now = LocalDate.now()
        val tradingName = "ACME Breads"
        val irdNumber = "111-111-111"
        val theCompany = Company("ACME Limited",
                tradingName,
                "123",
                "123",
                now,
                Month.JUNE,
                GstStatus.REGISTERED,
                now, irdNumber)

        whenever(company.getCompany()).thenReturn(theCompany)

        val addBusinessTransactionPostBody = AddBusinessTransactionPostBody(type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = null, amountInCents = 23000)
        val addBusinessTransactionJson: String = asJsonString(addBusinessTransactionPostBody)

        System.out.println(addBusinessTransactionJson)

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/business/transaction").content(addBusinessTransactionJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    fun asJsonString(obj: Any): String {
        try {
            val mapper = ObjectMapper()
            return mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

}