package io.tbp.microservice.endpoint

import io.tbp.microservice.domain.Example
import io.tbp.microservice.domain.Pagination
import io.tbp.microservice.service.interfaces.ExampleService
import io.tbp.microservice.util.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

/**
 *  Example endpoint <i>/api/examples</i> for operations the {@link Example} entity.
 *  <p> All requests and responses are JSON.
 */
@Component
class ExampleEndpoint extends GroovyChainAction {

    private static final int NO_CONTENT = 204
    private static final int CREATED = 201

    private final ExampleService service

    @Autowired
    ExampleEndpoint(ExampleService service) {
        this.service = service
    }

    @Override
    void execute() throws Exception {
        path(':name') {
            String name = pathTokens['name']
            byMethod {
                get {
                    service.findByName(name).then { example ->
                        render Jackson.json(example)
                    }
                }
                put {
                    parse(Jackson.fromJson(Example)).nextOp { example ->
                        service.update(example, name)
                    }.then { example ->
                        response.status(NO_CONTENT)
                        response.send()
                    }
                }
                delete {
                    service.delete(name).then {
                        response.status(NO_CONTENT)
                        response.send()
                    }
                }
            }
        }
        all {
            byMethod {
                get {
                    Pagination pagination = Utils.parsePaginationProperties(request.queryParams)
                    service.findAll(pagination).then { examples ->
                        render Jackson.json(examples)
                    }
                }
                post {
                    parse(Jackson.fromJson(Example)).nextOp { example ->
                        service.save(example)
                    }.then { example ->
                        response.status(CREATED)
                        Utils.addLocationHeader(response, "examples/${example.name}")
                        response.send()
                    }
                }
            }
        }
    }

}
