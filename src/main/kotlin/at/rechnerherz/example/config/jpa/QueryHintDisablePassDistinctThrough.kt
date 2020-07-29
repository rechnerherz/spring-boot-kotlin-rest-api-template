package at.rechnerherz.example.config.jpa

import org.springframework.data.jpa.repository.QueryHints
import javax.persistence.QueryHint

/**
 * Query hint to disable the pass-distinct-through mechanism.
 *
 * When doing a join it makes no sense to use pass distinct to the native SQL query, because all rows will be distinct,
 * and it is better for performance to omit it.
 *
 * [The DISTINCT pass-through Hibernate Query Hint](https://in.relation.to/2016/08/04/introducing-distinct-pass-through-query-hint/)
 */
@QueryHints(QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value = false.toString()))
annotation class QueryHintDisablePassDistinctThrough
