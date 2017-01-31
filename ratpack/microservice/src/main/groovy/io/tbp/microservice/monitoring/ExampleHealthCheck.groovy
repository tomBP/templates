package io.tbp.microservice.monitoring

import groovy.transform.CompileStatic
import io.tbp.microservice.domain.Pagination
import io.tbp.microservice.service.interfaces.ExampleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ratpack.exec.Promise
import ratpack.func.Function
import ratpack.health.HealthCheck
import ratpack.registry.Registry

/**
 * Checks the health of the Example service.
 */
@CompileStatic
@Component
class ExampleHealthCheck implements HealthCheck {

    private final ExampleService service

    @Autowired
    ExampleHealthCheck(ExampleService service) {
        this.service = service
    }

    @Override
    Promise<HealthCheck.Result> check(Registry registry) throws Exception {
        service.findAll(new Pagination(1, 0, 0)).map {
            HealthCheck.Result.healthy()
        }.mapError(Exception, { Exception ex ->
            HealthCheck.Result.unhealthy(ex.message)
        } as Function)
    }

    @SuppressWarnings('GetterMethodCouldBeProperty')
    @Override
    String getName() {
        'exampleHealth'
    }

}
