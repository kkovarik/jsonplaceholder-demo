## CLI for Jsonplaceholder

Simple CLI interface, does only support get user detail by its id

### Usage

Executable jar: `target/jsonplaceholder-cli.jar`.
```
Usage: jsonplaceholder-cli.jar <options>
Jsonplaceholder CLI
 -h,--host <host>       Backend host url (not mandatory)
 -u,--userId <userId>   Fetch detail of user identified by id
```

### Output

* JSON with response payload.
* logging is realized to STDERR stream

Example:
*java -jar jsonplaceholder-cli/target/jsonplaceholder-cli.jar --userId 2*
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
