package io.tbp.microservice.endpoint

import io.tbp.microservice.domain.Example
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.FieldDescriptor

import static com.jayway.restassured.RestAssured.given
import static groovy.json.JsonOutput.toJson
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.is
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.request.RequestDocumentation.*

/**
 * Document and test the <i>/api/examples</i> endpoint.
 */
@SuppressWarnings(['AbcMetric', 'MethodSize'])
class ExampleEndpointFunctionalSpec extends BaseDocumentationSpec {

    // CONSTANTS

    private static final String NAME_PARAM = 'name'
    private static final String NAME_DESC = "The example's name"
    private static final String LIMIT = 'limit'
    private static final String PAGE = 'page'
    private static final String PAGE_SIZE = 'pageSize'
    private static final String LIMIT_DESC = 'The maximum number of records to retrieve. If not ' +
            'provided, all records will be retrieved'
    private static final String PAGE_DESC = 'Which page of paginated result to retrieve. If not ' +
            'provided, the results will not be paginated'
    private static final String PAGE_SIZE_DESC = 'The number of results per page. If not ' +
            'provided, 10 will be used'

    // TESTS

    void 'Test and document getting a single user'() {
        expect:
        given(super.documentationSpec).
                contentType(JSON).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('example-get',
                        pathParameters(
                                parameterWithName(NAME_PARAM).
                                        description(NAME_DESC)
                        ),
                        responseFields(fields(''))
                )).
                when().get(EXAMPLE_RESOURCE, 'a@example.com').
                then().assertThat().
                statusCode(is(HttpStatus.OK.value())).
                body('name', equalTo('a@example.com')).
                body('description', equalTo("I'm example a.")).
                body('age', equalTo(23)).
                body('website', equalTo('http://a.com'))
    }

    void "Test getting a single that can't be found"() {
        expect:
        given(documentationSpec).
                contentType(JSON).
                accept(JSON).
                port(aut.address.port).
                when().get(EXAMPLE_RESOURCE, 'not-found').
                then().assertThat().statusCode(is(HttpStatus.NOT_FOUND.value()))
    }

    void 'Test and document getting a list of paginated users'() {
        expect:
        given(documentationSpec).
                contentType(JSON).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('example-list',
                        requestParameters(
                                parameterWithName(LIMIT).description(LIMIT_DESC),
                                parameterWithName(PAGE).description(PAGE_DESC),
                                parameterWithName(PAGE_SIZE).description(PAGE_SIZE_DESC)
                        ),
                        responseFields(fields('[].'))
                )).
                queryParam(LIMIT, '10').
                queryParam(PAGE, '1').
                queryParam(PAGE_SIZE, '2').
                when().get(EXAMPLE_ROOT).
                then().assertThat().
                statusCode(is(HttpStatus.OK.value())).
                body('size()', is(2)).
                body('[0].name', equalTo('a@example.com')).
                body('[0].description', equalTo("I'm example a.")).
                body('[0].age', equalTo(23)).
                body('[0].website', equalTo('http://a.com')).
                body('[1].name', equalTo('b@example.com')).
                body('[1].description', equalTo("I'm example b.")).
                body('[1].age', equalTo(32)).
                body('[1].website', equalTo('http://b.com'))
    }

    void 'Test and document adding a new user'() {
        given:
        Example newExample = new Example(
                name: 'f@example.com',
                description: "I'm example f.",
                age: 12,
                website: 'http://f.com',
        )
        expect:
        given(documentationSpec).
                contentType(JSON).
                body(toJson(newExample)).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('example-create', requestFields(fields('')))).
                when().post(EXAMPLE_ROOT).
                then().assertThat().
                statusCode(is(HttpStatus.CREATED.value())).
                header('Location', '/api/examples/f@example.com')
    }

    void 'Test and document updating an existing user'() {
        given:
        Example changedExample = new Example(
                name: 'a@example.com',
                description: 'Changed a.',
                age: 84,
                website: 'http://changed.com',
        )
        expect:
        given(documentationSpec).
                contentType(JSON).
                body(toJson(changedExample)).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('example-update',
                        pathParameters(parameterWithName(NAME_PARAM).description(NAME_DESC)),
                        requestFields(fields(''))
                )).
                when().put(EXAMPLE_RESOURCE, 'a@example.com').
                then().assertThat().statusCode(is(HttpStatus.NO_CONTENT.value()))
    }

    void 'Test and document updating an existing user not found'() {
        expect:
        given(documentationSpec).
                contentType(JSON).
                body(toJson(new Example(null, null, null, null))).
                accept(JSON).
                port(aut.address.port).
                when().put(EXAMPLE_RESOURCE, 'not-found').
                then().assertThat().
                statusCode(is(HttpStatus.NOT_FOUND.value()))
    }

    void 'Test and document deleting an existing user'() {
        expect:
        given(documentationSpec).
                contentType(JSON).
                accept(JSON).
                port(aut.address.port).
                filter(createFilter('example-delete',
                        pathParameters(parameterWithName(NAME_PARAM).description(NAME_DESC))
                )).
                when().delete(EXAMPLE_RESOURCE, 'a@example.com').
                then().assertThat().
                statusCode(is(HttpStatus.NO_CONTENT.value()))
    }

    FieldDescriptor[] fields(String prefix) {
        ConstrainedFields constrainedFields = new ConstrainedFields(Example)
        [
                constrainedFields.withPath("${prefix}name").
                        description('The name uniquely identifying the example'),
                constrainedFields.withPath("${prefix}description").
                        description('The complete description of the example'),
                constrainedFields.withPath("${prefix}age").description("The example's age"),
                constrainedFields.withPath("${prefix}website").description("The example's website")
        ]
    }

}
