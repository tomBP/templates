package io.tbp.microservice.exception.converter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.tbp.microservice.exception.ServiceError
import io.tbp.microservice.exception.QueryParameterException
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.jackson.JsonRender

import javax.annotation.PostConstruct

import static ratpack.jackson.Jackson.json

/**
 * Converts a {@link QueryParameterException}
 */
@CompileStatic
@Component
@Slf4j
class QueryParameterExceptionConverter implements ExceptionConverter<QueryParameterException> {

    private static final int STATUS = 400

    private final ExceptionConverterRegistry registry

    @Autowired
    QueryParameterExceptionConverter(ExceptionConverterRegistry registry) {
        this.registry = registry
    }

    @Override
    int exceptionStatusCode() {
        return STATUS
    }

    @Override
    void log(QueryParameterException exception) {
        log.error(exception.message)
    }

    @Override
    JsonRender renderApiError(QueryParameterException exception) {
        json(new ServiceError(
                status: STATUS,
                message: exception.message,
                timestamp: System.currentTimeMillis()
        ))
    }

    @PostConstruct
    void postConstruct() {
        registry.register(QueryParameterException, this)
    }

}
