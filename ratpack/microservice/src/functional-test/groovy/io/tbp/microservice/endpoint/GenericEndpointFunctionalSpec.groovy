package io.tbp.microservice.endpoint

import io.tbp.microservice.domain.Example
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor

import static com.jayway.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

/**
 * Documents and tests all behaviour not specific to a particular endpoint.
 */
class GenericEndpointFunctionalSpec extends BaseDocumentationSpec {

    void "Test and document error format"() {
        expect:
        given(documentationSpec).
                contentType(JSON).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('errors', responseFields(fields(false)))).
                when().get("${EXAMPLE_ROOT}/-1").
                then().assertThat().statusCode(is(HttpStatus.NOT_FOUND.value()))
    }

    void "Test and document validation error format"() {
        given:
        Example example = new Example(
                name: 'a',
                description: 'An a',
                age: -1,
                website: 'my-website'
        )
        expect:
        given(documentationSpec).
                contentType(JSON).
                body(example).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('validation-errors', responseFields(fields(true)))).
                when().post(EXAMPLE_ROOT).
                then().assertThat().statusCode(is(HttpStatus.BAD_REQUEST.value()))
    }

    FieldDescriptor[] fields(boolean validationFields) {
        List<FieldDescriptor> fields = [
                fieldWithPath('status').description('The HTTP status code, e.g. `404`'),
                fieldWithPath('subErrors').description('A more detailed breakdown of the error, ' +
                        'currently only applies for validation (400) errors'),
                fieldWithPath('message').description('A description of the cause of the error'),
                fieldWithPath('timestamp').description('The time, in milliseconds, at which the ' +
                        'error occurred')
        ]
        if (validationFields) {
            fields.addAll([
                    fieldWithPath('subErrors[].field').description('The field where the ' +
                            'validation error occurred'),
                    fieldWithPath('subErrors[].message').description('A full description of ' +
                            'the cause of the validation error'),
                    fieldWithPath('subErrors[].code').description('A code representing the ' +
                            'validation error'),
                    fieldWithPath('subErrors[].details').description('Details of the constraints ' +
                            'of the validation error'),
            ])
        }
        fields
    }

}
