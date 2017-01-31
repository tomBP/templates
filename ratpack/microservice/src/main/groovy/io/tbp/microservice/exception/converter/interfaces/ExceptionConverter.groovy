package io.tbp.microservice.exception.converter.interfaces

import groovy.transform.CompileStatic
import ratpack.jackson.JsonRender

/**
 * Defines the contract for converting API exceptions.
 */
@CompileStatic
interface ExceptionConverter<T extends Throwable> {

    /**
     * Log a suitable form of the exception
     */
    void log(T exception)

    /**
     * Get the HTTP status code representing the exception being converted.
     * @return the HTTP status code
     */
    int exceptionStatusCode()

    /**
     * Convert an exception into a suitable json representation.
     *
     * @param exception the exception to convert
     * @return the json representing the Exception
     */
    JsonRender renderApiError(T exception)

}
