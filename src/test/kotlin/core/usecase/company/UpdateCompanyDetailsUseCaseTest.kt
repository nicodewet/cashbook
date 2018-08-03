package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.CompanyStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.Month
import java.time.YearMonth

class UpdateCompanyDetailsUseCaseTest {

    @Test
    fun `when foo then bar`() {

        val company = Company("ACME Limited",
                null,
                "12345",
                "435345",
                YearMonth.now(),
                CompanyStatus.REGISTERED,
                Month.JUNE,
                null,
                null,
                null,
                null)

        val updateCompanyDetails = mockk<UpdateCompanyDetails>()

        every { updateCompanyDetails.updateDetails(any()) } returns company

    }

}
