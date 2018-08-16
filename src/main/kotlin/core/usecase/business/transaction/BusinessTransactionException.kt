package com.thorgil.cashbook.core.usecase.business.transaction

class BusinessTransactionException : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex) {}
    constructor(message: String): super(message) {}
    constructor(ex: Exception): super(ex) {}
}