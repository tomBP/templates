package io.tbp.microservice.endpoint.handler

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.netty.handler.codec.http.HttpResponseStatus
import io.tbp.microservice.exception.ServiceError
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.error.internal.ErrorHandler
import ratpack.handling.Context
import ratpack.http.Request
import io.tbp.microservice.exception.converter.NotFoundExceptionConverter
import ratpack.jackson.Jackson

/**
 * Central location for handling exceptions.
 * <p> To handle a new exception just create a new {@link ExceptionConverter} and register
 * it with the registry. See the {@link NotFoundExceptionConverter} for an example.
 */
@CompileStatic
@Slf4j
@Component
class GlobalErrorHandler implements ErrorHandler {

    private final ExceptionConverterRegistry registry

    @Autowired
    GlobalErrorHandler(ExceptionConverterRegistry registry) {
        this.registry = registry
    }

    // SERVER ERRORS

    @Override
    void error(Context ctx, Throwable throwable) throws Exception {
        ExceptionConverter converter = registry.get(throwable.class)
        converter.log(throwable)
        ctx.response.status converter.exceptionStatusCode()
        ctx.render converter.renderApiError(throwable)
    }

    // CLIENT ERRORS

    @Override
    void error(Context ctx, int statusCode) throws Exception {
        Request request = ctx.request
        HttpResponseStatus status = HttpResponseStatus.valueOf(statusCode)

        log.error("${statusCode} client error for request to ${request.rawUri}")
        ServiceError error = new ServiceError(
                status: status.code(),
                message: "Error accessing ${request.rawUri} by ${request.method.name}",
                timestamp: System.currentTimeMillis()
        )

        ctx.response.status statusCode
        ctx.render Jackson.json(error)
    }

}
