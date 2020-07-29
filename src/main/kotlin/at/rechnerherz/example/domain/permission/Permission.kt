package at.rechnerherz.example.domain.permission

/**
 * Permission passed to the [PermissionRuleEvaluator] to distinguish between read, write, and delete operations.
 */
enum class Permission {
    READ, WRITE, DELETE;

    /**
     * Compile-time string constants for method security expressions.
     */
    companion object {

        /**
         * Parameters for `hasPermission` on queries that need reference an entity by id, e.g. `findById(id)`.
         *
         * The target class is obtained from the calling repository using [PermissionRuleEvaluator.resolve].
         */
        const val ID_TARGET = "#id, @permissionRuleEvaluator.resolve(this)"

        /**
         * Parameters for `hasPermission` on  queries that return a collection (targetId = null), e.g. `findAll()`.
         *
         * The target class is obtained from the calling repository using [PermissionRuleEvaluator.resolve].
         */
        const val COLLECTION_TARGET = "null, @permissionRuleEvaluator.resolve(this)"

        /**
         * Parameters for `hasPermission` on queries that return a collection (targetId = null), e.g. `save(entity)`.
         */
        const val ENTITY_TARGET = "#entity"

        const val READ_PERMISSION = "T(at.rechnerherz.example.domain.permission.Permission).READ"
        const val WRITE_PERMISSION = "T(at.rechnerherz.example.domain.permission.Permission).WRITE"
        const val DELETE_PERMISSION = "T(at.rechnerherz.example.domain.permission.Permission).DELETE"
    }
}
