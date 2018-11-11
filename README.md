## JSONPlaceholder demo

* demo project for connecting to https://jsonplaceholder.typicode.com/
* technologies used: java, spring webflux, junit 5

### Structure
* `jsonplaceholder-core` : core library
* `jsonplaceholder-cli` : command line interface for core library
* `jsonplaceholder-web` : microservice wrapping the core library

### Build
* requirements: installed java 8+ & maven
* `mvn clean package`

### Running
* see README in modules `jsonplaceholder-cli` and `jsonplaceholder-web`.

