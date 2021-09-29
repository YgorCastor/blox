package me.ycastor.btc.domain.marketplace.core.errors

class InvalidCurrencyException(currency: String) : RuntimeException("No prices found for the '$currency' currency")
