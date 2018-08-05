package com.thorgil.cashbook.core.usecase.company

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.company.entity.GetCompanyUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month



class GetCompanyFromConfigurationTest {

    @Test
    fun `when configuration is correctly formatted we get a Company`() {

        // Arrange
        val companyConfiguration: GetCompany.CompanyConfiguration =
                GetCompany.CompanyConfiguration("Thorgil Limited",
                                                                "Thorgil Software",
                                                                "6833053",
                                                                "9429046765208",
                                                        "07-05-2018",
                                                    "JUNE",
                                                                "REGISTERED",
                                                    "07-05-2018",
                                                        "125-814-972")

        // Act
        val SUT: GetCompanyUseCase = GetCompanyUseCase(companyConfiguration)
        val company: Company = SUT.getCompany()

        // Assert
        assertThat(company.entityName).isEqualTo("Thorgil Limited")
        assertThat(company.tradingName).isEqualTo("Thorgil Software")
        assertThat(company.companyNumber).isEqualTo("6833053")
        assertThat(company.NZBN).isEqualTo("9429046765208")

        val incorporationDateTime = LocalDate.of(2018, Month.MAY, 7)
        assertThat(company.incorporationDate).isEqualTo(incorporationDateTime)
        assertThat(company.annualReturnFilingMonth).isEqualTo(Month.JUNE)
        assertThat(company.gstStatus).isEqualTo(GstStatus.REGISTERED)
        assertThat(company.gstEffectiveDate).isEqualTo(company.gstEffectiveDate)
        assertThat(company.irdNumber).isEqualTo("125-814-972")
    }

}