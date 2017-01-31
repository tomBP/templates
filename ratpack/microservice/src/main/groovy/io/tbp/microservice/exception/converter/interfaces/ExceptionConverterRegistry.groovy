package io.tbp.microservice.exception.converter.interfaces

import groovy.transform.CompileStatic

/**
 * Defines the behaviour for a registry of ExceptionConverters
 */
@CompileStatic
interface ExceptionConverterRegistry {

    /**
     * Get an ExceptionConverter from the register based on the exception it
     * converts.
     * @param key the exception class identifying the converter
     * @return the ExceptionConverter corresponding to the key
     */
    ExceptionConverter get(Class<? extends Throwable> key)

    /**
     * Add a new ExceptionConverter to the register keyed by the exception it
     * converts.
     * @param key the exception class identifying the converter
     * @param converter the ExceptionConverter to register
     */
    void register(Class<? extends Throwable> key, ExceptionConverter converter)

}
