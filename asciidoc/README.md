# Asciidoctor Template

Simple template for writing HTML documentation with [Asciidoctor](http://asciidoctor.org/)

## Install

First, make sure you've got Ruby installed. Then clone the repo and run:

```
gem install bundle && bundle install && bundle exec guard
```

This will start a web server and convert any `.adoc` files into `.html`. Any subsequent changes
made to the `.adoc` files will be automatically picked up.

## Config

Change the Guardfile to control the Asciidoctor generation.

