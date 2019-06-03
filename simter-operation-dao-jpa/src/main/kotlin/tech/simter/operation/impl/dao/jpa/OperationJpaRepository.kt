package tech.simter.operation.impl.dao.jpa

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import tech.simter.operation.impl.dao.jpa.po.OperationPo

/**
 * The block JPA-DAO Repository. See [CrudRepository], [PagingAndSortingRepository] and [SimpleJpaRepository].
 *
 * @author RJ
 * @author zh
 */
interface OperationJpaRepository : JpaRepository<OperationPo, String> {
  fun findByBatch(batch: String, sort: Sort): List<OperationPo>
  fun findByTargetTypeAndTargetId(targetType: String, targetId: String, sort: Sort): List<OperationPo>
}