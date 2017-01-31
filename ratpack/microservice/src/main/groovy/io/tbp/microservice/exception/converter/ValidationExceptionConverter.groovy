package io.tbp.microservice.exception.converter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.tbp.microservice.exception.ServiceError
import io.tbp.microservice.exception.ValidationException
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.jackson.JsonRender

import javax.annotation.PostConstruct

import static ratpack.jackson.Jackson.json

/**
 * Converts a {@link ValidationException}
 */
@CompileStatic
@Component
@Slf4j
class ValidationExceptionConverter implements ExceptionConverter<ValidationException> {

    private static final int STATUS = 400

    private final ExceptionConverterRegistry registry

    @Autowired
    ValidationExceptionConverter(ExceptionConverterRegistry registry) {
        this.registry = registry
    }

    @Override
    void log(ValidationException exception) {
        log.error(exception.toString())
    }

    @Override
    int exceptionStatusCode() {
        return STATUS
    }

    @Override
    JsonRender renderApiError(ValidationException exception) {
        json(new ServiceError(
                status: STATUS,
                message: 'Validation Error',
                subErrors: exception.errors,
                timestamp: System.currentTimeMillis()
        ))
    }

    @PostConstruct
    void postConstruct() {
        registry.register(ValidationException, this)
    }

}
