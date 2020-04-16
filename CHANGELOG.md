# simter-operation changelog

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