package tech.simter.operation.dao.jpa

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import tech.simter.operation.po.Operation

/**
 * The block JPA-DAO Repository. See [CrudRepository], [PagingAndSortingRepository] and [SimpleJpaRepository].
 *
 * @author RJ
 * @author zh
 */
interface OperationJpaRepository : JpaRepository<Operation, String> {
  fun findByBatch(batch: String, sort: Sort): List<Operation>
  fun findByTargetTypeAndTargetId(targetType: String, targetId: String, sort: Sort): List<Operation>
}