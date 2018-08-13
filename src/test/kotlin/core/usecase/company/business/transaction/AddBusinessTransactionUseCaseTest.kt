package com.thorgil.cashbook.core.usecase.company.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.entity.BusinessTransactionType
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionDTO
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.company.GetCompany
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class AddBusinessTransactionUseCaseTest {

    @Test
    fun `owner contribution made today is acceptable`() {

        // === Arrange ===

        val companyProvider: GetCompany = object: GetCompany {
            override fun getCompany(): Company {
                return Company(entityName = "ABC Limited",
                        companyNumber = "123",
                        NZBN = "123",
                        incorporationDate = LocalDate.now(),
                        annualReturnFilingMonth = Month.JUNE,
                        gstStatus = GstStatus.REGISTERED)
            }

        }

        val addBusinessTransactionInRepo: AddBusinessTransactionInRepository = object: AddBusinessTransactionInRepository {
            override fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction): Boolean {
                return true
            }

        }

        val addBusinessTransaction = AddBusinessTransactionDTO(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act ===

        val businessTransaction: BusinessTransaction = sut.addBusinessTransaction(addBusinessTransaction)

        // === Assert ===
        assertThat(businessTransaction.amountInCents).isEqualTo(2000)
        assertThat(businessTransaction.businessTransactionIssue).isEqualTo(null)
        assertThat(businessTransaction.businessTransactionIssueDetail).isEqualTo(null)
        assertThat(businessTransaction.childTransactions).isEqualTo(null)
        assertThat(businessTransaction.parentTransaction).isNull()
        assertThat(businessTransaction.completedDate).isNotNull()
        assertThat(businessTransaction.completedDate).isEqualTo(LocalDate.now())
        assertThat(businessTransaction.createdTimestamp).isNotNull()
        assertThat(businessTransaction.evidenceLink).isNull()
        assertThat(businessTransaction.gstInCents).isNotNull()
        assertThat(businessTransaction.scheduledDate).isNull()
        assertThat(businessTransaction.type).isEqualTo(BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT)
        assertThat(businessTransaction.uuid).isNotBlank()
        assertThat(businessTransaction.lastUpdateTimestamp).isNotNull()
    }

}