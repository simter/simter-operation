# Simter Operation Modules

## Requirement

- Maven 3.6+
- Kotlin 1.3+
- Java 8+
- Spring Framework 5.1+
- Spring Boot 2.1+
- Reactor 3.2+

## Maven Modules

| Sn | Name                                 | Type | Parent                        | Remark
|----|--------------------------------------|------|-------------------------------|--------
| 1  | [simter-operation]                   | pom  | [simter-build]                | Build these modules and define global properties and pluginManagement
| 2  | simter-operation-bom                 | pom  | simter-operation              | Bom of these modules
| 3  | simter-operation-parent              | pom  | simter-operation              | Define global dependencies and plugins
| 4  | simter-operation-data                | jar  | simter-operation-parent       | Service and Dao Interfaces
| 5  | simter-operation-data-reactive-mongo | jar  | simter-operation-parent       | Dao Implementation By Reactive MongoDB
| 6  | simter-operation-data-jpa            | jar  | simter-operation-parent       | Dao Implementation By JPA
| 7  | simter-operation-rest-webflux        | jar  | simter-operation-parent       | Rest API By WebFlux
| 8  | simter-operation-starter             | jar  | simter-operation-parent       | Microservice Starter


[simter-build]: https://github.com/simter/simter-build/tree/master
[simter-operation]: https://github.com/simter/simter-operation