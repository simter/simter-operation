package tech.simter.operation.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tech.simter.operation.po.Operation
import java.util.*

/**
 * The JPA implementation of [OperationBlockDao].
 *
 * @author RJ
 */
@Component
internal class OperationBlockDaoImpl @Autowired constructor(
  private val repository: OperationJpaRepository
) : OperationBlockDao {
  @Transactional(readOnly = true)
  override fun get(id: String): Optional<Operation> {
    return repository.findById(id)
  }

  @Transactional(readOnly = true)
  override fun findByCluster(cluster: String): List<Operation> {
    return repository.findByCluster(cluster, Sort(Sort.Direction.DESC, "time"))
  }

  @Transactional(readOnly = false)
  override fun create(vararg operations: Operation) {
    repository.saveAll(operations.toList())
  }
}