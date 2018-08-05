package com.thorgil.cashbook.core.usecase.company

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.usecase.company.entity.GetCompanyUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Month

class GetCompanyFromConfigurationTest {

    @Test
    fun `when configuration is correct we get a company`() {

        // Arrange
        val companyConfiguration: GetCompanyFromConfiguration.CompanyConfiguration =
                GetCompanyFromConfiguration.CompanyConfiguration("Thorgil Limited",
                                                                "Thorgil Software",
                                                                "6833053",
                                                                "9429046765208",
                                                        "07-05-2018",
                                                    "06",
                                                                "REGISTERED",
                                                    "07-05-2018",
                                                        "125-814-972")

        val companyConfigStub:GetCompanyFromConfiguration = object: GetCompanyFromConfiguration {
            override fun getCompanyConfiguration(): GetCompanyFromConfiguration.CompanyConfiguration {
                return companyConfiguration
            }
        }

        // Act
        val SUT: GetCompanyUseCase = GetCompanyUseCase(companyConfigStub)
        val company: Company = SUT.getCompanyDetails()

        // Assert
        assertThat(company.annualReturnFilingMonth).isEqualTo(Month.JUNE)

    }

}