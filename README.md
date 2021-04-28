# simter-operation

Business Operation Log Recorder.

## Maven Modules

| Sn | Name                          | Type | Parent                        | Remark
|----|-------------------------------|------|-------------------------------|--------
| 1  | [simter-operation]            | pom  | [simter-dependencies]                | Build these modules and define global properties and pluginManagement
| 2  | simter-operation-bom          | pom  | simter-operation              | Bom
| 3  | simter-operation-parent       | pom  | simter-operation              | Define global dependencies and plugins
| 4  | simter-operation-core         | jar  | simter-operation-parent       | Core API: [Operation], [OperationDao] and [OperationService]
| 5  | simter-operation-test         | jar  | simter-operation-parent       | Common unit test helper method
| 6  | simter-operation-dao-mongo    | jar  | simter-operation-parent       | [OperationDao] Implementation By Reactive MongoDB
| 7  | simter-operation-dao-jpa      | jar  | simter-operation-parent       | [OperationDao] Implementation By R2DBC
| 8  | simter-operation-dao-web      | jar  | simter-operation-parent       | [OperationDao] Implementation By WebFlux
| 9  | simter-operation-service-impl | jar  | simter-operation-parent       | Default [OperationService] Implementation
| 10 | simter-operation-rest-webflux | jar  | simter-operation-parent       | [Rest API] Implementation By WebFlux
| 11 | simter-operation-starter      | jar  | simter-operation-parent       | Microservice Starter

## Requirement

- Maven 3.6+
- Kotlin 1.4+
- Java 8+
- Spring Framework 5.3+
- Spring Boot 2.4+
- Reactor 3.4+


[simter-dependencies]: https://github.com/simter/simter-dependencies
[simter-operation]: https://github.com/simter/simter-operation
[Operation]: https://github.com/simter/simter-operation/blob/master/simter-operation-core/src/main/kotlin/tech/simter/operation/core/Operation.kt
[OperationDao]: https://github.com/simter/simter-operation/blob/master/simter-operation-core/src/main/kotlin/tech/simter/operation/core/OperationDao.kt
[OperationService]: https://github.com/simter/simter-operation/blob/master/simter-operation-core/src/main/kotlin/tech/simter/operation/core/OperationService.kt
[Rest API]: ./docs/rest-api.md