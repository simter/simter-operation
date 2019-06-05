package tech.simter.operation.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tech.simter.operation.core.Operation
import tech.simter.operation.impl.dao.jpa.po.OperationPo
import java.util.*
import javax.persistence.EntityManager

/**
 * The JPA implementation of [OperationBlockDao].
 *
 * @author RJ
 */
@Component
internal class OperationBlockDaoImpl @Autowired constructor(
  private val em: EntityManager,
  private val repository: OperationJpaRepository
) : OperationBlockDao {
  @Transactional(readOnly = false)
  override fun create(operation: Operation) {
    // do not use 'repository.save(po)' because it will select it first.
    // directly use 'EntityManager.persist(po)'.
    em.persist(operation)
  }

  @Suppress("unchecked_cast")
  @Transactional(readOnly = true)
  override fun get(id: String): Optional<Operation> {
    val entity: Optional<OperationPo> = repository.findById(id)
    if (entity.isPresent) {
      entity.get().items.size // load lazy collection
    }
    return entity as Optional<Operation>
  }

  @Transactional(readOnly = true)
  override fun findByBatch(batch: String): List<Operation> {
    return repository.findByBatch(batch, Sort.by(Sort.Direction.DESC, "ts"))
  }

  @Transactional(readOnly = true)
  override fun findByTarget(targetType: String, targetId: String): List<Operation> {
    return repository.findByTargetTypeAndTargetId(targetType, targetId, Sort.by(Sort.Direction.DESC, "ts"))
  }
}