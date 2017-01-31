package io.tbp.microservice.exception

import groovy.transform.CompileStatic

/**
 * Thrown when a resource could not be found
 */
@CompileStatic
class NotFoundException extends RuntimeException {

    NotFoundException(String message) {
        super(message)
    }

}
