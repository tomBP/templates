package io.tbp.microservice.util

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.tbp.microservice.domain.Pagination
import io.tbp.microservice.exception.QueryParameterException
import ratpack.http.Response

@CompileStatic
@Slf4j
final class Utils {

    private static final int DEFAULT_PAGE_SIZE = 10

    private Utils() { /* Prevent outside instantiation*/ }

    static void addLocationHeader(Response response, String loc) {
        response.headers.add('Location', "/api/${loc}")
    }

    /**
     * Create a pagination object from a request's query parameters.
     * Tries to parse the following parameters:
     * <ul>
     *     <li>page</li>
     *     <li>pageSize</li>
     *     <li>pageLimit</li>
     * </ul>
     * If a parameter could not be parsed then it will be set to 0.
     */
    static Pagination parsePaginationProperties(Map<String, String> params) {
        int limit = parse(params.limit, 'limit')
        int page = parse(params.page, 'page')
        int pageSize = parse(params.pageSize, 'pageSize')
        new Pagination(limit, page, pageSize)
    }

    /**
     * Paginate a list of objects.
     * First reduce the list size to the limit. Then return a list representing the page requested.
     */
    static <T> List<T> paginate(List<T> list, Pagination pagination) {
        List<T> limitedList = pagination.limit ?
                list.subList(0, Math.min(list.size(), pagination.limit)) : list
        if (pagination.page) {
            int pageSize = pagination.pageSize ?: DEFAULT_PAGE_SIZE
            int offset = pageSize * (pagination.page - 1)
            limitedList.subList(
                    Math.min(offset, limitedList.size()),
                    Math.min(offset + pageSize, limitedList.size())
            )
        } else {
            limitedList
        }
    }

    private static int parse(String value,
                             String name) {
        try {
            return value ? Integer.parseInt(value) : 0
        } catch (NumberFormatException ignored) {
            throw new QueryParameterException("${name} must be a positive integer")
        }
    }

}
