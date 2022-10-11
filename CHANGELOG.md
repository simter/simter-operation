# simter-operation changelog

## 2.1.0 - 2022-10-11

- Upgrade to simter-3.3.0 (spring-boot-2.7.4)

## 2.0.0 - 2022-06-27

- Upgrade to simter-3.0.0 (spring-boot-2.7.0 and Java-17)
- Upgrade embedded mongo config (spring-boot-2.7 drops Embedded Mongo 3.4 supported)
- Upgrade `r2dbc-postgresql` groupId to `org.postgresql` (Since 0.9+)
- Set starter to no r2dbc-pool config by default
- Add r2dbc-pool dependency to real database profile

## 2.0.0-M4 - 2022-01-13

- Upgrade to simter-3.0.0-M6 (spring-boot-2.6.2)
- Set starter to no r2dbc-pool config by default
- Fixed kotlin-1.6+ compile error on `@JvmField`
    > JvmField cannot be applied to a property that overrides some other property

## 2.0.0-M3 - 2021-08-10

- Upgrade to simter-3.0.0-M3 (spring-boot-2.5.3)
- Add convenient dao method: `create(type, targretId, ....)`
    > auto get context user as operator
- Simplify unit test class name
  > Such as rename `Delete...Test` to `DeleteTest`
- Response BadRequest status if raise SerializationException
- Use kotlinx-serialization to deal the integration test json code
- Separate `server-url` to `server.url` and `server.context-path`
- Use kotlinx-serialization to convert obj to json on dao-web/OperationDaoImpl.create
    > Instead of javax.json.Json
- Refactor rest-unit-test code to use configurable context-path
- Refactor rest-integration-test code to use configurable context-path
- Polishing rest-api.md to make context-path more clear
- Fixed `GET /operation` api implementation
    > Compatible with `GET /operation/`
- Fixed `POST /operation` api implementation
    > Compatible with `POST /operation/`

## 2.0.0-M2 - 2021-04-28

- Upgrade to simter-3.0.0-M2 (spring-boot-2.4.5)
- Use ·tech.simter.kotlin.data.Page` instead of `org.springframework.data.domain.Page`
- Upgrade code to use kotlinx-serialization

## 2.0.0-M1 - 2020-12-22

- Upgrade to simter-3.0.0-M1 (spring-boot-2.4.1)
- Add `@Transactional` config on `OperationServiceImpl`
- Use spring-data-r2dbc instead of r2dbc-client
- Add integration test for find pageable operations
- Add integration test for find by batch
- Add integration test for find by target
- Fixed POST url missing trailing slash error
    > For compatible with next spring-boot-2.4

## 1.0.1 - 2021-01-21

- Fixed POST url missing trailing slash error
    > For compatible with next spring-boot-2.4

## 1.0.0 - 2020-11-20

- Upgrade to simter-2.0.0 (spring-boot-2.3.6)
- First stable version.

## 0.11.0 2020-06-05

- Upgrade to simter-2.0.0-M1 - start spring-boot-2.3.0
- Add module simter-operation-service-impl
- Add module simter-operation-test
- Move ImmutableOperation to Operation.Impl

## 0.10.0 2020-05-08

- Upgrade to simter-1.3.0-M15

## 0.9.0 2020-04-16

- Upgrade to simter-1.3.0-M14 (kotlin-1.3.70+)

## 0.8.0 2020-04-09

- Log main config property on starter with info level
- Rename property 'module.rest-context-path.simter-operation' to 'simter-operation.rest-context-path'
- Rename property 'module.server-address.simter-operation' to 'simter-operation.server-address'
- Add maven property 'server.port'
- Rename property 'module.authorization.simter-operation' to 'simter-operation.authorization'
- Package starter finalName with platform type as suffix
- Add more profiles on dao-r2dbc - embedded-mysql, mysql, mssql
- Add more profiles on starter - r2dbc-embedded-mysql, r2dbc-mysql, r2dbc-mssql
- Rename default prod database name st_operation to operation
- Add db.options property for embedded-h2
- Minimize HiKari pool config
- Use stable spring version for r2dbc
- Add mssql SQL
- Upgrade to simter-1.3.0-M13

## 0.7.0 2020-01-09

- Upgrade to simter-1.3.0-M11

## 0.7.0-M2 2019-11-21

- Upgrade to simter-1.3.0-M6

## 0.7.0-M1 2019-11-18

- Upgrade to simter-1.3.0-M5
- Add new pageable operations data API and its implementation.

## 0.6.1 2019-09-04

- Remove unstable dependency reactor-kotlin-extensions-1.0.0.M1

## 0.6.0 2019-07-05

- Upgrade to simter-1.2.0
- Use stable spring version on main branch
- Use milestone spring version for r2dbc
- Support jpa-eclipselink-x profiles on starter module
- Fixed EclipseLink config on starter module
- Polishing pom.xml for deploy to bintray
- Add pom.xml header line `<?xml version="1.0" encoding="UTF-8"?>`

## 0.5.0 2019-06-28

- Add web dao implementation [#18](https://github.com/simter/simter-operation/issues/18)
- Implement Authorization header on dao-web module [#23](https://github.com/simter/simter-operation/issues/23)
- Implement rest-api `GET /$id` [#22](https://github.com/simter/simter-operation/issues/22)

## 0.5.0-M5 2019-06-11

- Fixed error setting JsonIgnoreProperties on jpa po
- Fixed jpa persistence failed. [#24](https://github.com/simter/simter-operation/issues/24)
- Fixed missing filed setting

## 0.5.0-M4 2019-06-10

- Add missing interface parameters on OperationService.Create
- Use `@Repository` instead of `@Component` on dao implementation
- Polishing docs and README

## 0.5.0-M3 2019-06-05

- Set json default to use ISO format
- Use reactor-kotlin-extensions prepare for next reactor version
- Implements dao-web by WebFlux
- Remove charset from JSON content type
- Compatible with spring-data 2.1 and 2.2
- Add r2dbc-xxx profiles to starter
- Implement dao by r2dbc-client
- Config favicon.ico on starter module
- Upgrade to simter platform 1.2.0-M6
- Refactor module structure to make core api simplify and clear [#21](https://github.com/simter/simter-operation/issues/21)

## 0.5.0-M2 2019-05-15

- Eager load Operation.items
- Upgrade to simter platform 1.2.0-M5
- Delete unnecessary dependency javax.json-api
- Add convenient create method to service
- Move MockBean to UnitTestConfiguration

## 0.5.0-M1 2019-05-06

- Upgrade to simter platform 1.2.0.M4
- Add SQL script for H2, hsql, derby, mysql, see [simter-operation-data/.../sql/](simter-operation-data/src/main/resources/tech/simter/operation/sql).
- Support EclipseLink on module simter-operation-data-jpa

Breaking change: [#17](https://github.com/simter/simter-operation/issues/17)

- Rename Operation.time to Operation.ts.
- Rename Operation.comment to Operation.remark.
- Rename Operation.cluster to Operation.batch.
- Operation.operator change to Operation.operatorId and Operation.operatorName
- Operation.target change to Operation.targetType and Operation.targetId
- Rename dao.findByCluster to dao.findByBatch.
- Add dao.findByTarget.
- Add rest /target/{targetType/{targetId}, see [docs/rest-api.md](docs/rest-api.md).

## 0.4.0 2019-01-14

- Upgrade to simter-build-1.1.0 and simter-dependencies-1.1.0

## 0.3.0 2019-01-10

- Change `Dao|Service.create(operation: Operation)` to `Dao|Service.create(vararg operations: Operation)`
- Upgrade to simter platform 1.0.0

## 0.2.0 2018-12-04

- Upgrade to simter platform 0.7.0 (with jpa-2.2)

## 0.1.0 2018-10-16

- Initial base on simter platform 0.6.0