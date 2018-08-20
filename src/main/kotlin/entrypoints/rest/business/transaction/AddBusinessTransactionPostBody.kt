package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransactionType

/**
 * @param scheduledDate must be in ISO 8601 local date format e.g. 2018-07-31
 * @param completedDate must be in ISO 8601 local date format e.g. 2018-07-31
 */
data class AddBusinessTransactionPostBody(val type: BusinessTransactionType,
                                          val parentTransactionUUID: String? = null,
                                          val scheduledDate: String? = null,
                                          val completedDate: String?,
                                          val amountInCents: Int,
                                          val gstInCents: Int = 0,
                                          val evidenceLink: String? = null)