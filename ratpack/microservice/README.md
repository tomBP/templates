# Microservice template

A simple template for creating production microservices with Ratpack, Groovy and Spring Boot.

## Contents

1. [Introduction](#intro)
2. [Quick Start](#quickstart)
3. [Guide](#guide)
    - [Config](#config)
    - [Formatter/IntelliJ setup](#ide)
4. [Appendix](#appendix)
    - [Cheatsheet](#cheatsheet)
    
## <a name="intro"></a> Introduction

Aims to make writing a production microservice with Ratpack easier. It provides the following:

* [Gradle](https://gradle.org/) build
* Written in typed, statically compiled [Groovy](http://www.groovy-lang.org/)
* Integration with [Spring Boot](http://projects.spring.io/spring-boot/)
* Integration with [RxJava](https://github.com/ReactiveX/RxJava)
* Aync [Log4j2 Logging](http://logging.apache.org/log4j/2.x/)
* Metric websocket endpoint via [Dropwizard metrics](http://metrics.dropwizard.io/3.1.0/)
* Health Check endpoints
* Simple handling of errors and translation to Json responses
* Yaml config files activated with Spring profiles 
* Validation with (Hibernate Validator)[http://hibernate.org/validator/]
* Test written with [Spock](http://spockframework.github.io/spock/docs/1.0/index.html)
* Code linted with [CodeNarc](http://codenarc.sourceforge.net/)
* Documentation generated from functional tests using 
[SpringRestDocs](http://projects.spring.io/spring-restdocs/) 
* Code formatted according to Google and StandardJs guidelines

## <a name="quickstart"></a> Quick start 

### Gradle

```bash
$ git clone https://github.com/tomBP/templates.git
$ cd templates/ratpack/microservice
$ gradlew build run
```

Endpoints:

* Documentation - [http://localhost:5050] 
* Service root - `http://localhost:5050/api` 
* Metrics - `http://localhost:5050/admin/metrics` 
* Health check - `http://localhost:5050/admin/health-check/exampleHealth` 

### Native executable

```bash
$ ./gradlew assemble
$ cd build/distributions
$ tar -xvf tar -xvf microservice-${version}.tar
$ ./microservice-${version}/bin/api
```

See the [cheatsheet](#cheatsheet) below for a full list of run options.

## <a name="guide"></a> Guide

### <a name="config"></a> Configuration 

Ratpack loads configuration files based on the concept of a
[Base Dir](https://ratpack.io/manual/current/launching.html#base_dir). This is effectively the file
system root of the application and is discovered by placing a `.ratpack` file in the desired 
directory. In keeping with conventions it is set to `src/main/resources`. Configuration specific to 
Ratpack is loaded from ratpack-application.yml

All other config is handled by Spring and loaded from `application.yml`. Spring allows for extra 
config files to overload `application.yml` by activating a profile and adding a new yml file in 
the form `application-{profileName}.yml`. The profile can be activated by provided a `mode` system
property or a `SERVICE_ENV` environment variable.

### <a name="ide"></a> Formatter/IDE Setup

Having followed the quickstart instructions above just point your IDE to the project's location
and import it as a Gradle project.

#### Groovy/Java (IntelliJ)

Download the `intellij-java-google-style.xml` file from the [Google](http://code.google.com/p/google-styleguide/) repo.
Copy it into the `config/codestyles` folder in your IntelliJ settings folder. In Linux/Windows this will normally be 
`$HOME/.IntelliJIdea15` and for mac `$HOME/Library/Preferences/IntelliJIdea15`. You may have to create the `codestyles` 
dir if it doesn't already exist. Then under _Settings->Preferences->Editor->Code Style_ select google-styleguide as 
the current code style.

Install the [Save Actions Plugin](https://plugins.jetbrains.com/plugin/7642). Activate the plugin
and in the settings select the _Reformat Code_ and _Reformat Only Changed Code_ options.

#### JavaScript

Follow the [StandardJS](https://github.com/feross/standard/blob/master/docs/webstorm.md) instructions.

## <a name="appendix"></a> Appendix

### <a name="cheatsheet"></a> Command Line Cheatsheet

*Everything (tests, doc, codequality, distribution):* `./gradlew build`

*Clean:* `./gradlew clean`

*Run:* `./gradlew run`

*Run with hot reload:* `./gradlew -t run`

*Run with remote debug:* `./gradlew run --debug-jvm`

*Unit tests:* `./gradlew test`

*Functional tests:* `./gradlew functionalTest`

*Single test:* `./gradlew :repository:test --tests "*${test name}*"`

*Docs only:* `./gradlew asciidoctor`

*Linter:* `./gradlew codenarcMain codenarcTest codenarcFunctionalTest`

*Distribution only:* `./gradlew assemble`
