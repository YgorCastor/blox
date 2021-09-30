package me.ycastor.btc.commons.errorhandler

import io.quarkus.arc.ArcUndeclaredThrowableException
import javax.ws.rs.core.Response
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

@ServerExceptionMapper(ApplicationException::class)
fun applicationException(exception: ApplicationException): Response {
    val statusCode = exception.error.status
    return Response.status(statusCode)
        .entity(exception.error)
        .build()
}

@ServerExceptionMapper(Throwable::class)
fun unknownException(exception: Throwable): Response =
    Response.serverError()
        .entity(
            UnknownError()
        )
        .build()

@ServerExceptionMapper(ArcUndeclaredThrowableException::class)
fun arcException(exception: ArcUndeclaredThrowableException): Response =
    when (exception.cause) {
        is ApplicationException ->
            applicationException(exception = exception.cause as ApplicationException)
        else                    -> {
            val unwrappedException =
                exception.cause
                ?: exception
            unknownException(unwrappedException)
        }
    }