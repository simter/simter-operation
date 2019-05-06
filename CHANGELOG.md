# simter-operation changelog

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