package io.tbp.microservice.exception.converter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.tbp.microservice.exception.ServiceError
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import ratpack.jackson.Jackson
import ratpack.jackson.JsonRender

/**
 *  Generic ExceptionConverter not tied to any particular exception.
 */
@CompileStatic
@Slf4j
class DefaultExceptionConverter implements ExceptionConverter<Throwable> {

    private static final int STATUS = 500

    @Override
    int exceptionStatusCode() {
        return STATUS
    }

    @Override
    void log(Throwable ex) {
        log.error('The following uncontrolled error occurred: ', ex)
    }

    @Override
    JsonRender renderApiError(Throwable exception) {
        Jackson.json(new ServiceError(
                status: STATUS,
                message: exception.message,
                timestamp: System.currentTimeMillis()
        ))
    }

}
