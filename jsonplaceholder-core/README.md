### Core library

* library for accessing REST API (https://jsonplaceholder.typicode.com/)
* its entrypoint interface is `cz.kkovarik.jsonplaceholder.core.JsonPlaceholderClient`,
with one implementation `cz.kkovarik.jsonplaceholder.core.impl.DefaultJsonPlaceholderClient`
* implementation does use Spring WebClient to make REST calls non-blocking, see oficial
documentation for more info: https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client
* packaged as jar file, meant to be included in other projects, recommended is to use it in
Spring WebFlux reactive stack
