package tech.simter.operation.impl.dao.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tech.simter.operation.core.Operation
import tech.simter.operation.core.OperationView
import tech.simter.operation.impl.dao.jpa.po.OperationPo
import tech.simter.operation.impl.dao.jpa.po.OperationViewPo
import tech.simter.util.StringUtils.CaseType.LowerCase
import tech.simter.util.StringUtils.underscore
import java.util.*
import javax.persistence.EntityManager
import kotlin.reflect.full.memberProperties

/**
 * The JPA implementation of [OperationBlockDao].
 *
 * @author RJ
 */
@Repository
internal class OperationBlockDaoImpl @Autowired constructor(
  private val em: EntityManager,
  private val repository: OperationJpaRepository
) : OperationBlockDao {
  @Transactional(readOnly = false)
  override fun create(operation: Operation) {
    // do not use 'repository.save(po)' because it will select it first.
    // directly use 'EntityManager.persist(po)'.
    em.persist(OperationPo.from(operation))
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

  // cache operationView property names
  private val operationViewPropertyNames = underscore(
    source = OperationView::class.memberProperties.joinToString(",") { it.name },
    caseType = LowerCase
  ).split(",")

  @Suppress("unchecked_cast")
  @Transactional(readOnly = true)
  override fun find(targetTypes: List<String>?, pageNo: Int, pageSize: Int, search: String?): Page<Operation> {
    val conditions = mutableListOf<String>()
    targetTypes?.let { conditions.add("target_type in :targetTypes") }
    search?.let { conditions.add("title like :search or operator_name like :search") }
    val conditionQ = conditions.joinToString(" and ")
    val whereQ = if (conditions.isEmpty()) "" else "where $conditionQ"
    val fromQ = "from st_operation s"
    val countQ = "select count(0) $fromQ $whereQ"
    val rowQ = """
        select ${operationViewPropertyNames.joinToString(", ")}
        $fromQ
        $whereQ
        order by s.ts desc
        """.trimIndent()

    val rowsQuery = em.createNativeQuery(rowQ, OperationViewPo::class.java)
      .setFirstResult(tech.simter.data.Page.calculateOffset(pageNo, pageSize))
      .setMaxResults(tech.simter.data.Page.toValidCapacity(pageSize))
    val countQuery = em.createNativeQuery(countQ)

    targetTypes?.let {
      rowsQuery.setParameter("targetTypes", it)
      countQuery.setParameter("targetTypes", it)
    }
    search?.let {
      val searchStr = if (it.contains("%")) it else "%$it%"
      rowsQuery.setParameter("search", searchStr.trim())
      countQuery.setParameter("search", searchStr.trim())
    }

    return PageImpl(
      rowsQuery.resultList as List<Operation>,
      PageRequest.of(pageNo - 1, pageSize),
      (countQuery.singleResult as Number).toLong()
    )
  }
}