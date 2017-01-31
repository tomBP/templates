package io.tbp.microservice.service.interfaces

import groovy.transform.CompileStatic
import io.tbp.microservice.domain.Example
import io.tbp.microservice.domain.Pagination
import ratpack.exec.Operation
import ratpack.exec.Promise

@CompileStatic
interface ExampleService {

    Operation delete(String name)

    Promise<List<Example>> findAll(Pagination pagination)

    Promise<Example> findByName(String name)

    Operation update(Example example, String name)

    Operation save(Example example)

}
