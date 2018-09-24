package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Following Google JSON Style Guide
 *
 * https://google.github.io/styleguide/jsoncstyleguide.xml
 *
 * Using @JsonInclude(JsonInclude.Include.NON_EMPTY) as per Google JSON Style Guide rule: Consider removing empty or
 * null values.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BusinessTransactionDTO(val uuid: String,
                                  val createdTimestamp: LocalDateTime,
                                  val lastUpdateTimestamp: LocalDateTime,
                                  val scheduledDate: LocalDate?,
                                  val completedDate: LocalDate?,
                                  val type: String,
                                  val amountInCents: Int,
                                  val gstInCents: Int,
                                  val parentTransactionUuid: String?,
                                  val childTransactionUuids: List<String>?,
                                  val evidenceLink: String?,
                                  val businessTransactionIssue: String?,
                                  val businessTransactionIssueDetail: String?)