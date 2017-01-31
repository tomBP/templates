package io.tbp.microservice.exception

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class ValidationException extends RuntimeException {

    List<ServiceErrorDetail> errors

}
