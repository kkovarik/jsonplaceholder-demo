## Jsonplaceholder microservice

Microservice to expose REST API for the core library.

### Running

#### Executable jar
`java -jar target/jsonplaceholder-web.jar`
* by default, port 8080 is used (http://localhost:8080), can be configured via `server.port` property.

#### Docker container
`docker-compose up`
* basic `Dockerfile` & `docker-compose.yml` are provided
* port 8080 is forwarded from container to the host

### Endpoints

#### Business endpoints (/api/*)
* /api/users/<id> : get user detail for user identified by its id

Example response:
```
{
   "name":"Leanne Graham",
   "username":"Bret",
   "email":"Sincere@april.biz",
   "posts":[
      {
         "id":"1",
         "title":"sunt aut facere repellat provident occaecati excepturi optio reprehenderit"
      },
      ...
   ]
}
```
#### Actuator endpoints (/actuator/*)

* endpoints to verify microservice state or analysis, see https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

### Configuration properties
* `jsonplaceholder.url` : the url of the website

### Implementation
* based on Spring Webflux https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux
to achieve reactive non-blocking stack.
