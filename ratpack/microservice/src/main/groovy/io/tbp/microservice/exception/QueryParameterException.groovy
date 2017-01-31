package io.tbp.microservice.exception

import groovy.transform.CompileStatic

/**
 * Thrown when a query parameter is invalid.
 */
@CompileStatic
class QueryParameterException extends RuntimeException {

    QueryParameterException(String message) {
        super(message)
    }

}
