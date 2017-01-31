package io.tbp.microservice.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class Pagination {

    /**
     * The number of results to retrieve
     */
    int limit

    /**
     * Which page of results to retrieve
     */
    int page

    /**
     * Number of results per page
     */
    int pageSize

}
