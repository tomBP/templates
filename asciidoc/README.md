# Asciidoctor Template

Simple template for writing HTML documentation with [Asciidoctor](http://asciidoctor.org/)

## Install

First, make sure you've got Ruby installed. Then clone this repo and run:

```
$ gem install bundle && bundle install && bundle exec guard
```

This will convert any `.adoc` files in the directory into `.html`. Any subsequent changes
made to the `.adoc` files will be automatically picked up.

Navigate to `file:///{install-path}/templates/asciidoc/example.html` to see an example.

## Config

Change the Guardfile to control the Asciidoctor generation.
