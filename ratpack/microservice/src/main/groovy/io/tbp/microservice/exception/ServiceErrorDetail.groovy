package io.tbp.microservice.exception

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class ServiceErrorDetail {

    String code

    List<String> details

    String field

    String message

}
