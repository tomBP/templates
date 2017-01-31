package io.tbp.microservice.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.URL

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@CompileStatic
@Immutable
class Example {

    @Email
    @NotNull
    String name

    @Size(max = 30)
    @Pattern(regexp = "^I\'m.*")
    String description

    @Min(1L)
    @Max(150L)
    Integer age

    @URL
    String website

}
