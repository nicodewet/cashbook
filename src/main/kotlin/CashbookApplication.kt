package com.thorgil.cashbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CashbookApplication

fun main(args: Array<String>) {
    runApplication<CashbookApplication>(*args)
}
