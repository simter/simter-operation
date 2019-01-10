# Simter operation record Modules

## Requirement

- Maven 3.6+
- Kotlin 1.3+
- Java 8+
- Spring Framework 5+
- Spring Boot 2+
- Reactor 3+

## Maven Modules

Sn | Name                                   | Parent                        | Remark
---|----------------------------------------|-------------------------------|--------
1  | [simter-operation-build]               | [simter-build:1.0.0]          | Build modules and define global properties and pluginManagement
2  | [simter-operation-dependencies]        | simter-operation-build        | Define global dependencyManagement
3  | [simter-operation-parent]              | simter-operation-dependencies | All sub modules parent module, Define global dependencies and plugins
4  | [simter-operation-data]                | simter-operation-parent       | Define Service and Dao Interfaces
5  | [simter-operation-data-reactive-mongo] | simter-operation-parent       | Dao Implementation By Reactive MongoDB
6  | [simter-operation-data-jpa]            | simter-operation-parent       | Dao Implementation By JPA
7  | [simter-operation-rest-webflux]        | simter-operation-parent       | Rest API By WebFlux
8  | [simter-operation-starter]             | simter-operation-parent       | Microservice Starter


Remark : Module 1, 2, 3 all has maven-enforcer-plugin and flatten-maven-plugin config. Other modules must not configure them.


[simter-build:1.0.0]: https://github.com/simter/simter-build/tree/1.0.0
[simter-operation-build]: https://github.com/simter/simter-operation
[simter-operation-dependencies]: https://github.com/simter/simter-operation/tree/master/simter-operation-dependencies
[simter-operation-parent]: https://github.com/simter/simter-operation/tree/master/simter-operation-parent
[simter-operation-data]: https://github.com/simter/simter-operation/tree/master/simter-operation-data
[simter-operation-data-jpa]: https://github.com/simter/simter-operation/tree/master/simter-operation-data-jpa
[simter-operation-data-reactive-mongo]: https://github.com/simter/simter-operation/tree/master/simter-operation-data-reactive-mongo
[simter-operation-rest-webflux]: https://github.com/simter/simter-operation/tree/master/simter-operation-rest-webflux
[simter-operation-starter]: https://github.com/simter/simter-operation/tree/master/simter-operation-starter