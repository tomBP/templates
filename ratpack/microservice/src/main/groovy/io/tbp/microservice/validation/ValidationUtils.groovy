package io.tbp.microservice.validation

import groovy.transform.CompileStatic
import io.tbp.microservice.exception.ServiceErrorDetail
import ratpack.exec.Promise
import rx.Observable

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

/**
 * Utility class providing functionality for validating domain objects.
 */
@CompileStatic
final class ValidationUtils {

    private static final String MAX_KEY = '{javax.validation.constraints.Max.message}'
    private static final String MIN_KEY = '{javax.validation.constraints.Min.message}'
    private static final String NOT_NULL_KEY = '{javax.validation.constraints.NotNull.message}'
    private static final String PATTERN_KEY = '{javax.validation.constraints.Pattern.message}'
    private static final String SIZE_KEY = '{javax.validation.constraints.Size.message}'
    private static final String EMAIL_KEY = '{org.hibernate.validator.constraints.Email.message}'
    private static final String URL_KEY = '{org.hibernate.validator.constraints.URL.message}'

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().validator

    private ValidationUtils() { /* Prevent outside instantiation */ }

    /**
     * Validate an object according to its <tt>javax.validation.constraints</tt> annotations.
     * <p> <tt>ConstraintViolations</tt> are converted into <tt>ValidationErrors</tt>
     * which contain the affected fields and the error messages.
     * @param domainObject the javax annotated object to validate
     * @return the list of ValidationErrors or an empty list if the object is valid
     */
    static <T> Promise<List<ServiceErrorDetail>> validate(T domainObject) {
        Observable.from(VALIDATOR.validate(domainObject)).map { ConstraintViolation violation ->
            createFromViolation(violation)
        }.promise()
    }

    // HELPER METHODS

    private static ServiceErrorDetail createFromViolation(ConstraintViolation<?> violation) {
        String field = violation.propertyPath
        String message = violation.message
        String code = ''
        List<String> details = []
        switch (violation.messageTemplate) {
            case MAX_KEY:
                code = 'VE1'
                details = [getViolationAttribute(violation, 'value')]
                break
            case MIN_KEY:
                code = 'VE2'
                details = [getViolationAttribute(violation, 'value')]
                break
            case NOT_NULL_KEY:
                code = 'VE3'
                details = []
                break
            case PATTERN_KEY:
                code = 'VE4'
                details = [getViolationAttribute(violation, 'regexp')]
                break
            case SIZE_KEY:
                code = 'VE5'
                String min = getViolationAttribute(violation, 'min')
                String max = getViolationAttribute(violation, 'max')
                details = [min, max]
                break
            case EMAIL_KEY:
                code = 'VE6'
                details = []
                break
            case URL_KEY:
                code = 'VE7'
                details = []
                break
        }
        new ServiceErrorDetail(
                code: code,
                details: details,
                field: field,
                message: message,
        )
    }

    private static String getViolationAttribute(ConstraintViolation<?> violation,
                                                String key) {
        violation.constraintDescriptor.attributes.get(key).toString()
    }

}
