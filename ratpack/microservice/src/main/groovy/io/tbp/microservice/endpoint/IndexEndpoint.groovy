package io.tbp.microservice.endpoint

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.springframework.stereotype.Component
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

/**
 *  Displays all the API endpoints
 */
@Component
class IndexEndpoint extends GroovyChainAction {

    @Override
    void execute() throws Exception {
        get {
            Endpoint example = new Endpoint('Example', '/api/examples')
            render Jackson.json([example])
        }
    }

    @Immutable
    @CompileStatic
    private static class Endpoint {
        String resourceName
        String url
    }

}
