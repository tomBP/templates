package io.tbp.microservice.exception.converter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.tbp.microservice.exception.ServiceError
import io.tbp.microservice.exception.NotFoundException
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.jackson.Jackson
import ratpack.jackson.JsonRender

import javax.annotation.PostConstruct

/**
 * Converts a {@link NotFoundException}
 */
@CompileStatic
@Component
@Slf4j
class NotFoundExceptionConverter implements ExceptionConverter<NotFoundException> {

    private static final int STATUS = 404

    private final ExceptionConverterRegistry registry

    @Autowired
    NotFoundExceptionConverter(ExceptionConverterRegistry registry) {
        this.registry = registry
    }

    @Override
    int exceptionStatusCode() {
        return STATUS
    }

    @Override
    void log(NotFoundException ex) {
        log.error(ex.message)
    }

    @Override
    JsonRender renderApiError(NotFoundException exception) {
        Jackson.json(new ServiceError(
                status: STATUS,
                message: exception.message,
                timestamp: System.currentTimeMillis()
        ))
    }

    @PostConstruct
    void postConstruct() {
        registry.register(NotFoundException, this)
    }

}
