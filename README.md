# Catch them all

Catch-them-all provides a simple HTTP API endpoint to obtain a Shakespeare description for a given pokemon.

## How to run it

Pull the docker image:
```docker pull ghcr.io/azanin/catch-them-all/catch-them-all:latest```

Run it:
 ```docker run -d -e TRANSLATE_HOST='https://api.funtranslations.com' -e POKEMON_HOST='https://pokeapi.co/api/v2' -p 5000:80 ghcr.io/azanin/catch-them-all/catch-them-all:latest```

To interact with the service you can make a curl:
```curl localhost:5000/pokemon/charizard```

## Swagger 
After the server is up and running you can check the swagger endpoint at:
```http://localhost:5000/docs```

## Integration tests
To run integration tests you need `sbt` installed. 
[Here](https://www.scala-sbt.org/1.x/docs/Setup.html) you can find how to install it.

You can type then `sbt integrationTests`

Integration tests task automatically produce a fat jar and build a docker image from that.
Thanks to [test container library](https://github.com/testcontainers/testcontainers-scala)
I have created two different containers interacting with each other.

The `Api Server` container ìnteracts with a `MockServer` container to test different scenarios.

## Continuous Integration
Github actions ensure that unit test and integration tests run every time a PR is opened and a merge in the main branch happens.
There is also a `publish` step that publishes the tagged image to the GitHub Container Registry.
You can find all the published images [here](https://github.com/users/azanin/packages/container/package/catch-them-all%2Fcatch-them-all)

## Dockerfile
CI flow rely on [sbt-docker](https://github.com/marcuslonnberg/sbt-docker) plugin to generate Docker images.
Looking at the `build.sbt` you can easily guess the generated Dockerfile.

```def dockerFile(dependsOn: File) = {
     val artifactTargetPath = s"/app/${dependsOn.name}"
   
     new Dockerfile {
       from("openjdk:11-jre")
       add(dependsOn, artifactTargetPath)
       entryPoint("java", "-jar", artifactTargetPath)
       expose(80)
       label("org.containers.image.source", s"https://github.com/azanin/${projectName}")
     }
   }
```
The generated file is:
````FROM openjdk:11-jre
    ADD 0/catch-them-all.jar /app/catch-them-all.jar
    ENTRYPOINT ["java", "-jar", "\/app\/catch-them-all.jar"]
    EXPOSE 80
    LABEL org.containers.image.source="https://github.com/azanin/catch-them-all"
````

I added a Dockerfile as well that you can use to build and run a container of the api without relying on `sbt`.
