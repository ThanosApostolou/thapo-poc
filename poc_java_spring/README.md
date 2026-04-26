# pocspring

This project now includes a Spring Boot-managed gRPC server backed by the proto contracts in `src/main/resources/proto`.

## gRPC endpoint

- Host: `localhost`
- Port: `19090`
- Service: `thapo.pocspring.graphql.v1.GraphqlApiService`

## Proto source

- `src/main/resources/proto/message.proto`
- `src/main/resources/proto/service.proto`

The build generates protobuf message classes from the edition-2024 proto files and uses a hand-written `GraphqlApiServiceGrpc` adapter class for gRPC stubs/service binding.

## Useful commands

```zsh
export JAVA_HOME=/usr/lib/jvm/java-26-openjdk
./gradlew clean generateProto compileJava
./gradlew test
./gradlew bootRun
```

## Implemented RPCs

- `Hello`
- `GetTest`
- `ListBooks`
- `GetBookById`
- `AddBook`
- `BookAdded`

