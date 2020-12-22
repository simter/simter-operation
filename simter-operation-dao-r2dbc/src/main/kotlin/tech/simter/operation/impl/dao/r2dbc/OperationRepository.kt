package tech.simter.operation.impl.dao.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import tech.simter.operation.impl.dao.r2dbc.po.OperationPo

/**
 * The reactive repository.
 *
 * @author RJ
 */
interface OperationRepository : R2dbcRepository<OperationPo, String>