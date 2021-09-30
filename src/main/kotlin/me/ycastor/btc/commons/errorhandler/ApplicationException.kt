package me.ycastor.btc.commons.errorhandler

class ApplicationException(val error: Error) : RuntimeException(error.message)
