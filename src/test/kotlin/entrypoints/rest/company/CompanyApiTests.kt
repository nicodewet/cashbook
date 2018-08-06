package com.thorgil.cashbook.entrypoints.rest.company

import com.nhaarman.mockito_kotlin.whenever
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.company.GetCompany
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

@ExtendWith(SpringExtension::class)
@WebMvcTest
// TODO Use constructor-based val property for @MockBean when supported, see issue spring-boot#13113
class CompanyHttpApiTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var company: GetCompany

    @Test
    fun `get company`() {
        val now = LocalDate.now()
        val theCompany = Company("ACME Limited",
                "ACME Breads",
                "123",
                "123",
                now,
                Month.JUNE,
                GstStatus.REGISTERED,
                now, "111-111-111")

        whenever(company.getCompany()).thenReturn(theCompany)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/company").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.entityName").value(theCompany.entityName))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.companyNumber").value(theCompany.companyNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.nzbn").value(theCompany.NZBN))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.annualReturnFilingMonth").value(theCompany.annualReturnFilingMonth.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.gstStatus").value(theCompany.gstStatus.toString()))
    }

}