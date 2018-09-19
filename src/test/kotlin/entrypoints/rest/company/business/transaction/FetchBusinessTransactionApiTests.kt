package com.thorgil.cashbook.entrypoints.rest.company.business.transaction

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.entity.BusinessTransactionType
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.business.transaction.FetchBusinessTransactionsUseCase
import com.thorgil.cashbook.core.usecase.company.GetCompany
import com.thorgil.cashbook.entrypoints.rest.business.transaction.BusinessTransactionApiEndpoint
import org.hamcrest.Matchers
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
class FetchBusinessTransactionApiTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var fetchBusinessTransactionsUseCase: FetchBusinessTransactionsUseCase

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
    }

    @Test
    fun `fetch business transactions by year and month - expected list returned`() {

        // Arrange
        val completedDate = LocalDate.now()
        val businessTransaction = BusinessTransaction(completedDate = completedDate,
                                                        scheduledDate = null,
                                                        type = BusinessTransactionType.INVOICE_PAYMENT,
                                                        amountInCents = 100,
                                                        gstInCents = 15)
        val businessTransactions = listOf(businessTransaction)

        whenever(fetchBusinessTransactionsUseCase.fetchBusinessTransactions(any())).thenReturn(businessTransactions)
        whenever(company.getCompany()).thenReturn(companyProvider.getCompany())

        // Act and Assert
        val yearDateThatIsNotSignificantInThisTestCase = "2018-06"
        mockMvc.perform(MockMvcRequestBuilders.get(
                        BusinessTransactionApiEndpoint.BUSINESS_TRANSACTION_END_POINT_URL + "?period="
                                    + yearDateThatIsNotSignificantInThisTestCase)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uuid", Matchers.equalTo(businessTransaction.uuid) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdTimestamp", Matchers.not(Matchers.isEmptyOrNullString()) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastUpdateTimestamp", Matchers.not(Matchers.isEmptyOrNullString()) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scheduledDate", Matchers.equalTo(businessTransaction.scheduledDate) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].completedDate", Matchers.equalTo(completedDate.toString()) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type", Matchers.equalTo(BusinessTransactionType.INVOICE_PAYMENT.toString()) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountInCents", Matchers.equalTo(businessTransaction.amountInCents) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gstInCents", Matchers.equalTo(businessTransaction.gstInCents) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].parentTransaction", Matchers.equalTo(null) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].childTransactions", Matchers.equalTo(null) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].evidenceLink", Matchers.equalTo(null) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].businessTransactionIssue", Matchers.equalTo(null) ))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].businessTransactionIssueDetail", Matchers.equalTo(null) ))
    }
}