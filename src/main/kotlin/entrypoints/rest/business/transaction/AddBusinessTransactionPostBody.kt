package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransactionType

data class AddBusinessTransactionPostBody(val type: BusinessTransactionType,
                                          val parentTransactionUUID: String? = null,
                                          val scheduledDate: String? = null,
                                          val completedDate: String?,
                                          val amountInCents: Int,
                                          val gstInCents: Int = 0,
                                          val evidenceLink: String? = null)