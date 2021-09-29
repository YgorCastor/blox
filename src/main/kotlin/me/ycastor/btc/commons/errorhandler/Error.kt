package me.ycastor.btc.commons.errorhandler

abstract class Error(
    open val code: String,
    open val message: String,
    open val status: Int,
    open val details: List<String> = emptyList(),
)
