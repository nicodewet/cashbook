package com.thorgil.cashbook.core.usecase.company.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.entity.BusinessTransactionType
import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddParentBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import com.thorgil.cashbook.core.usecase.company.GetCompany
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.Month

class AddLinkedBusinessTransactionUseCaseTest {

    companion object {

        private val LOGGER = LoggerFactory.getLogger(AddParentBusinessTransactionUseCaseTest::class.java)

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
    fun `can add a transaction, then another with the previous as parent and the link shows up in both when fetched`() {
        // === Arrange ===

        val addParentBusinessTransaction = AddBusinessTransactionMessage(
                type = BusinessTransactionType.INVOICE_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000,
                gstInCents = 0
        )

        val sut = AddParentBusinessTransactionUseCase(AddLinkedBusinessTransactionUseCaseTest.companyProvider, AddLinkedBusinessTransactionUseCaseTest.addBusinessTransactionInRepo)

        val parentBusinessTransaction: BusinessTransaction = sut.addParentBusinessTransaction(addParentBusinessTransaction)

        val addChildBusinessTransaction = AddBusinessTransactionMessage(
                // NOTE the link between the transaction below
                parentTransactionUUID = parentBusinessTransaction.uuid,
                type = BusinessTransactionType.IRD_GST_PAYMENT,
                completedDate = LocalDate.now(),
                amountInCents = 2000,
                gstInCents = 0
        )

        // === Act & Assert ===

        // TODO change this test case so we use a suitable linked business trans method
        org.junit.jupiter.api.Assertions.assertThrows(BusinessTransactionException::class.java) {
            sut.addParentBusinessTransaction(addChildBusinessTransaction)
        }
        // val childBusinessTransaction: BusinessTransaction = sut.addParentBusinessTransaction()

        // === Assert ===
        // Assertions.assertThat(childBusinessTransaction.parentTransaction).isNull()

        // TODO expect childTransaction to have specified parent uuid
        // TODO fetch child and expect the above again
        // TODO fetch parent and expect specified child transaction

    }
}