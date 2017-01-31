package io.tbp.microservice.service

import groovy.transform.CompileStatic
import io.tbp.microservice.domain.Example
import io.tbp.microservice.domain.Pagination
import io.tbp.microservice.exception.NotFoundException
import io.tbp.microservice.service.interfaces.ExampleService
import io.tbp.microservice.util.Utils
import io.tbp.microservice.validation.ExampleValidator
import org.springframework.stereotype.Service
import ratpack.exec.Operation
import ratpack.exec.Promise

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
@Service
class InMemoryExampleService implements ExampleService {

    private final Map<String, Example> examples = new ConcurrentHashMap<>()

    @Override
    Operation delete(String name) {
        Operation.of { examples.remove(name) }
    }

    @Override
    Promise<List<Example>> findAll(Pagination pagination) {
        Promise.value(Utils.paginate(
                examples.values().sort { a, b -> a.name <=> b.name },
                pagination
        ))
    }

    @Override
    Promise<Example> findByName(String name) {
        Promise.sync { examples.get(name) }.onNull {
            throw new NotFoundException("The example with name: ${name} could not be found")
        }.map { Example example ->
            example
        }
    }

    @Override
    Operation update(Example example, String name) {
        Promise.sync { examples.get(name) }.onNull {
            throw new NotFoundException("The example with name: ${name} could not be found")
        }.nextOp {
            ExampleValidator.validate(it)
        }.next {
            examples.put(name, example)
        }.operation()
    }

    @Override
    Operation save(Example example) {
        Promise.value(example).nextOp {
            ExampleValidator.validate(it)
        }.next {
            examples.put(it.name, it)
        }.operation()
    }

    @PostConstruct
    void insertExampleData() {
        Example exampleA = new Example('a@example.com', "I'm example a.", 23, 'http://a.com')
        examples.put(exampleA.name, exampleA)
        Example exampleB = new Example('b@example.com', "I'm example b.", 32, 'http://b.com')
        examples.put(exampleB.name, exampleB)
        Example exampleC = new Example('c@example.com', "I'm example c.", 43, 'http://c.com')
        examples.put(exampleC.name, exampleC)
        Example exampleD = new Example('d@example.com', "I'm example d.", 34, 'http://d.com')
        examples.put(exampleD.name, exampleD)
        Example exampleE = new Example('e@example.com', "I'm example e.", 99, 'http://e.com')
        examples.put(exampleE.name, exampleE)
    }

}
