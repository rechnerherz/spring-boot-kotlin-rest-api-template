package at.rechnerherz.example.config.jpa

import org.hibernate.dialect.MariaDB103Dialect
import org.hibernate.type.StandardBasicTypes
import java.sql.Types

/**
 * Customized Hibernate MariaDB dialect.
 *
 * Extends from [MariaDB103Dialect], which provides [fractional seconds support](https://dev.mysql.com/doc/refman/5.7/en/fractional-seconds.html).
 *
 * Maps BIGINT to Java Long instead of BigInteger.
 */
class CustomMariaDB103Dialect : MariaDB103Dialect() {

    init {

        // Map JDBC BIGINT to Java Long instead of BigInteger
        // https://hibernate.atlassian.net/browse/hhh-7318
        registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.name)
    }
}
