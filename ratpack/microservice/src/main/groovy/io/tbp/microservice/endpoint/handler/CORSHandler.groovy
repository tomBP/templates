package io.tbp.microservice.endpoint.handler

import groovy.transform.CompileStatic
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.http.MutableHeaders

/**
 * Add CORS headers so the API can be accessed from different origins.
 */
@CompileStatic
class CORSHandler implements Handler {

    @Override
    void handle(Context ctx) throws Exception {
        MutableHeaders headers = ctx.response.headers
        headers.set('Access-Control-Allow-Origin', '*')
        headers.set('Access-Control-Allow-Headers', 'x-requested-with, origin, content-type, accept')
        headers.set('Access-Control-Allow-Methods', 'DELETE, GET, OPTIONS, PATCH, POST, PUT')
        ctx.next()
    }

}
