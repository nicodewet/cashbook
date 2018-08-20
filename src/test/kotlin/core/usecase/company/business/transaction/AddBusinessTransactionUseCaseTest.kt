package com.thorgil.cashbook.core.usecase.company.business.transaction

import com.thorgil.cashbook.core.entity.*
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import com.thorgil.cashbook.core.usecase.company.GetCompany
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.Month

class AddBusinessTransactionUseCaseTest {

    companion object {

        private val LOGGER = LoggerFactory.getLogger(AddBusinessTransactionUseCaseTest::class.java)

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

        private val addBusinessTransactionInRepo: AddBusinessTransactionInRepository = object: AddBusinessTransactionInRepository {
            override fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction) {
            }

        }

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            LOGGER.info("beforeAll called")
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            LOGGER.info("afterAll called")
        }
    }

    @Test
    fun `owner contribution made today is acceptable and has no associated GST`() {

        // === Arrange ===

        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000,
                gstInCents = 0
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
        assertThat(businessTransaction.gstInCents).isEqualTo(0)
        assertThat(businessTransaction.scheduledDate).isNull()
        assertThat(businessTransaction.type).isEqualTo(BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT)
        assertThat(businessTransaction.uuid).isNotBlank()
        assertThat(businessTransaction.lastUpdateTimestamp).isNotNull()
    }

    @Test
    fun `invoice transaction has the appropriate associated GST`() {

        // === Arrange ===

        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000,
                gstInCents = 300
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act ===

        val businessTransaction: BusinessTransaction = sut.addBusinessTransaction(addBusinessTransaction)

        // === Assert ===

        assertThat(businessTransaction.businessTransactionIssue).isEqualTo(null)
        assertThat(businessTransaction.businessTransactionIssueDetail).isEqualTo(null)
        assertThat(businessTransaction.gstInCents).isEqualTo(300)
    }

    @Test
    fun `invoice transaction with incorrect associated GST has an issue`() {

        // === Arrange ===

        val addBusinessTransWithIncorrectGST = AddBusinessTransactionMessage(
                type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000,
                gstInCents = 3
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act ===

        val businessTransaction: BusinessTransaction = sut.addBusinessTransaction(addBusinessTransWithIncorrectGST)

        // === Assert ===

        assertThat(businessTransaction.businessTransactionIssue).isEqualTo(BusinessTransactionIssue.INVOICE_PAYMENT_WITH_INCORRECT_GST)
        assertThat(businessTransaction.businessTransactionIssueDetail).isNotBlank()
        assertThat(businessTransaction.gstInCents).isEqualTo(3)
    }

    @Test
    fun `completed transaction cannot also have a scheduled date`() {
        // === Arrange ===

        val tomorrow = LocalDate.now().plusDays(1)
        val addBusinessTransWithIncorrectSchedDate = AddBusinessTransactionMessage(
                type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.now(),
                scheduledDate = tomorrow,
                amountInCents = 2000,
                gstInCents = 300
        )
        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act & Assert ===
        Assertions.assertThrows(BusinessTransactionException::class.java) {
            sut.addBusinessTransaction(addBusinessTransWithIncorrectSchedDate)
        }

    }

    @Test
    fun `amount field must be a positive value`() {
        // === Arrange ===

        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = -2
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act & Assert ===
        Assertions.assertThrows(BusinessTransactionException::class.java) {
            sut.addBusinessTransaction(addBusinessTransaction)
        }
    }

    @Test
    fun `GST must be zero or more when supplied and is less than zero`() {
        // === Arrange ===

        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 500,
                gstInCents = -1
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act & Assert ===
        Assertions.assertThrows(BusinessTransactionException::class.java) {
            sut.addBusinessTransaction(addBusinessTransaction)
        }
    }

    @Test
    fun `cannot schedule a business transaction for a past date`() {
        // === Arrange ===

        val yesterday = LocalDate.now().minusDays(1)
        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 500,
                gstInCents = 575,
                scheduledDate = yesterday
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        Assertions.assertThrows(BusinessTransactionException::class.java) {
            sut.addBusinessTransaction(addBusinessTransaction)
        }
    }

    @Test
    fun `can schedule a business transaction for a future date`() {
        // === Arrange ===

        val thirtyDaysFromNow = LocalDate.now().plusDays(30)
        val addBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.OWNER_CONTRIBUTION_PAYMENT,
                completedDate = null,
                amountInCents = 500,
                gstInCents = 575,
                scheduledDate = thirtyDaysFromNow
        )

        val sut = AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)

        // === Act ===

        val businessTransaction: BusinessTransaction = sut.addBusinessTransaction(addBusinessTransaction)

        // === Assert ===

        assertThat(businessTransaction.businessTransactionIssue).isEqualTo(null)
        assertThat(businessTransaction.businessTransactionIssueDetail).isEqualTo(null)
    }

}