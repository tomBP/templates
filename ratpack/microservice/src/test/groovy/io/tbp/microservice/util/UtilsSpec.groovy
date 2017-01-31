package io.tbp.microservice.util

import io.tbp.microservice.domain.Pagination
import io.tbp.microservice.exception.QueryParameterException
import spock.lang.Specification
import spock.lang.Unroll

class UtilsSpec extends Specification {

    @Unroll
    def 'ParsePaginationProperties Page:#page PageSize: #pageSize Limit:#limit'() {
        setup:
        Map<String, String> params = [
                'page'    : page,
                'pageSize': pageSize,
                'limit'   : limit
        ]

        when:
        Pagination result = Utils.parsePaginationProperties(params)

        then:
        result.page == expectedPage
        result.pageSize == expectedPageSize
        result.limit == expectedLimit

        where:
        page | pageSize | limit | expectedPage | expectedPageSize | expectedLimit
        '1'  | '5'      | '10'  | 1            | 5                | 10
        ''   | ''       | ''    | 0            | 0                | 0
        null | null     | null  | 0            | 0                | 0
    }

    def 'ParsePaginationProperties parse error'() {
        setup:
        Map<String, String> params = ['page': 'error',]

        when:
        Utils.parsePaginationProperties(params)

        then:
        thrown QueryParameterException
    }

    @Unroll
    def 'Paginate Limit:#limit Page:#page PageSize:#pageSize'() {
        setup:
        List<Integer> list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        Pagination pagination = new Pagination(limit, page, pageSize)

        when:
        List<Integer> result = Utils.paginate(list, pagination)

        then:
        result == expectedList

        where:
        limit | page | pageSize | expectedList
        11    | 0    | 2        | [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        11    | 1    | 2        | [1, 2]
        11    | 2    | 2        | [3, 4]
        11    | 6    | 2        | [11]
        11    | 5    | 3        | []
        1     | 0    | 0        | [1]
        5     | 3    | 2        | [5]
    }

}
