package io.tbp.microservice

import io.tbp.microservice.endpoint.ExampleEndpoint
import io.tbp.microservice.endpoint.IndexEndpoint
import io.tbp.microservice.endpoint.handler.CORSHandler
import org.springframework.boot.Banner
import org.springframework.boot.builder.SpringApplicationBuilder
import ratpack.dropwizard.metrics.DropwizardMetricsConfig
import ratpack.dropwizard.metrics.DropwizardMetricsModule
import ratpack.dropwizard.metrics.MetricsWebsocketBroadcastHandler
import ratpack.groovy.Groovy
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.groovy.template.TextTemplateModule
import ratpack.guice.Guice
import ratpack.handling.RequestLogger
import ratpack.health.HealthCheckHandler
import ratpack.registry.Registry
import ratpack.rx.RxRatpack
import ratpack.server.BaseDir
import ratpack.server.RatpackServer
import ratpack.service.Service
import ratpack.session.SessionModule
import ratpack.spring.Spring

/**
 *  API entry point
 */
class Application {

    @SuppressWarnings(['AbcMetric', 'MethodSize'])
    static void main(String[] args) {

        RatpackServer.start { spec ->

            Registry springRegistry = createSpringRegistry()

            spec.serverConfig { conf ->
                conf.with {
                    // Base config dir defined by presence of .ratpack file (src/main/resources)
                    baseDir BaseDir.find()
                    // Load ratpack specific config
                    yaml 'ratpack-application.yml'
                    // Load environment variables prefixed with RATPACK_
                    env()
                    // Load system properties prefixed with ratpack.
                    sysProps()
                    // Load dropwizard config under /metrics
                    require('/metrics', DropwizardMetricsConfig)
                }
            }

            spec.registry(
                    Guice.registry { reg ->
                        reg.with {
                            // Dropwizard module
                            moduleConfig(DropwizardMetricsModule, DropwizardMetricsConfig)
                            // Required for html/text templates
                            module MarkupTemplateModule
                            module TextTemplateModule
                            // Required for HTTP sessions and Pack4J
                            module SessionModule
                            // Bootstrap RxJava
                            bindInstance(Service, { RxRatpack.initialize() } as Service)
                        }
                    }
            )

            spec.handlers(Groovy.chain {

                // Add Spring
                register(springRegistry)

                // Log all requests
                all RequestLogger.ncsa()

                // Serve static content from the public dir
                files {
                    it.dir('public').indexFiles('index.html')
                }

                // Endpoints
                prefix('api') {

                    // Add CORS headers to all api responses
                    all new CORSHandler()

                    // Index
                    all chain(springRegistry.get(IndexEndpoint))

                    // Examples
                    prefix('examples') {
                        all chain(springRegistry.get(ExampleEndpoint))
                    }
                }

                // By default redirect to the API documentation
                get {
                    redirect('/docs/html5')
                }

                // Admin routes
                prefix('admin') {
                    // Check whether the server started with error
                    get('up') {
                        render 'Application running'
                    }
                    // Health checks for external services - database etc
                    get('health-check/:name?', new HealthCheckHandler())
                    // Dropwizard metrics in json format
                    get('metrics-report', new MetricsWebsocketBroadcastHandler())
                    get('metrics') {
                        render Groovy.groovyTemplate('metrics.html')
                    }
                }
            })
        }
    }

    // HELPER METHODS

    /**
     * Load the embedded Spring Application.
     * <p>Activates a Spring profile by concatenating the {@code mode} system property and
     * {@code SERVICE_ENV} environment variable if set. This allows for config files of
     * the form application-${profile}.yml to be picked up and override the default application.yml
     * config.
     */
    private static Registry createSpringRegistry() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder().
                bannerMode(Banner.Mode.OFF).
                sources(SpringApplication)
        setSpringProfile(builder)
        Spring.spring(builder)
    }

    private static setSpringProfile(SpringApplicationBuilder builder) {
        String mode = System.getProperty('mode')
        String env = System.getenv('SERVICE_ENV')
        String profile = (mode ?: '') + (env ?: '')
        if (profile) {
            builder.profiles(profile)
        }
    }

}

