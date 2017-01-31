package io.tbp.microservice.exception.converter

import groovy.transform.CompileStatic
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverter
import io.tbp.microservice.exception.converter.interfaces.ExceptionConverterRegistry
import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap

/**
 * Concurrent map based registry of ExceptionConverters.
 * <p> This registry will return a DefaultExceptionConverter if no converter could be found
 * for the passed in key.
 */
@CompileStatic
@Component
class ConcurrentMapConverterRegistry implements ExceptionConverterRegistry {

    private final DefaultExceptionConverter defaultConverter = new DefaultExceptionConverter()
    private final Map<Class<? extends Throwable>, ExceptionConverter> exceptions =
            new ConcurrentHashMap<>()

    @Override
    ExceptionConverter get(Class<? extends Throwable> key) {
        exceptions.get(key) ?: defaultConverter
    }

    @Override
    void register(Class<? extends Throwable> key, ExceptionConverter converter) {
        exceptions.put(key, converter)
    }

}
