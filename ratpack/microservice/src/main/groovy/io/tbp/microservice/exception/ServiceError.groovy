package io.tbp.microservice.exception

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class ServiceError {

    /**
     * An short explanation of the error
     */
    String message

    /**
     * The HTTP status code representing the error
     */
    int status

    /**
     * Detailed breakdown of the error (currently only applies to validation (400) errors)
     */
    List<ServiceErrorDetail> subErrors

    /**
     * The time in milliseconds when the error occurred
     */
    long timestamp

}
