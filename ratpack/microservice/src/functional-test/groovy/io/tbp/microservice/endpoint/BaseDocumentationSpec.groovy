package io.tbp.microservice.endpoint

import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.specification.RequestSpecification
import groovy.transform.CompileStatic
import io.tbp.microservice.Application
import org.junit.Rule
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.restassured.RestDocumentationFilter
import org.springframework.restdocs.snippet.Snippet
import org.springframework.util.StringUtils
import ratpack.test.MainClassApplicationUnderTest
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.snippet.Attributes.key

/**
 * Base class for functional tests providing documentation
 */
@CompileStatic
class BaseDocumentationSpec extends Specification {

    private static final String[] REQUEST_HEADERS_TO_REMOVE =
            ['Host', 'Accept', 'Content-Type', 'Content-Length']
    private static final String[] RESPONSE_HEADERS_TO_REMOVE =
            ['Access-Control-Allow-Origin', 'Access-Control-Allow-Headers',
             'Access-Control-Allow-Methods', 'content-type', 'set-cookie', 'content-encoding',
             'transfer-encoding', 'connection', 'Content-Length']
    protected static final String EXAMPLE_ROOT = 'api/examples'
    protected static final String EXAMPLE_RESOURCE = "${EXAMPLE_ROOT}/{name}"
    protected static final String JSON = 'application/json'
    protected RequestSpecification documentationSpec

    /**
     * Bootstrap the application to test
     */
    @AutoCleanup
    @Shared
    MainClassApplicationUnderTest aut = new MainClassApplicationUnderTest(Application)

    /**
     * Set the location of the generated documentation snippets
     */
    @Rule
    JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation('build/generated-snippets')

    /**
     * Runs once before each test.
     * Setup the documentation spec
     */
    void setup() {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build()
    }

    /**
     * Convenience method to create a documentation filter with
     * preprocessed requests and responses
     */
    RestDocumentationFilter createFilter(String name, Snippet... snippets) {
        document(name,
                preprocessRequest(
                        prettyPrint(),
                        removeHeaders(REQUEST_HEADERS_TO_REMOVE)
                ),
                preprocessResponse(
                        prettyPrint(),
                        removeHeaders(RESPONSE_HEADERS_TO_REMOVE)
                ),
                snippets
        )
    }

    /**
     * Documents the field constraints of a domain class
     */
    protected static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input)
        }

        /**
         * Augment the fieldWithPath method to include javax.validation constraint
         * messages.
         * <p> Messages are separated by a full stop followed by a space. Vertical bars not at the
         * start of lines are delimited with a backlash as otherwise asciidoc interprets them as
         * table columns.
         */
        protected FieldDescriptor withPath(String path,
                                           String... additionalConstraints) {
            List<String> descriptions = this.constraintDescriptions.descriptionsForProperty(path)
            descriptions.addAll(additionalConstraints)
            return fieldWithPath(path)
                    .attributes(key('constraints')
                    .value(StringUtils.collectionToDelimitedString(descriptions, '. ')
                    .replaceAll('(?<!^)\\|', '\\\\|')))
        }
    }

}
