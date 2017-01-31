package io.tbp.microservice.validation

import groovy.transform.CompileStatic
import io.tbp.microservice.domain.Example
import io.tbp.microservice.exception.ServiceErrorDetail
import io.tbp.microservice.exception.ValidationException
import ratpack.exec.Operation

@CompileStatic
final class ExampleValidator {

    private ExampleValidator() { /* Prevent outside instantiation */ }

    static Operation validate(Example example) {
        Operation.of {
            ValidationUtils.validate(example).then { List<ServiceErrorDetail> errors ->
                if (errors) {
                    throw new ValidationException(errors)
                }
            }
        }
    }

}

