package at.rechnerherz.example.config.auth

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import mu.KotlinLogging
import org.springframework.stereotype.Service

/**
 * Service to warn about and block too many login attempts.
 *
 * [Prevent Brute Force Authentication Attempts with Spring Security](http://www.baeldung.com/spring-security-block-brute-force-authentication-attempts).
 */
@Service
class LoginAttemptService(
    private val loginAttemptProperties: LoginAttemptProperties
) {

    private val log = KotlinLogging.logger {}

    /**
     * A [CacheLoader] used for building a [LoadingCache]
     * that returns 0 when a key that is not in the cache is accessed.
     */
    private object StringIntCacheLoader : CacheLoader<String, Int>() {
        override fun load(key: String): Int = 0
    }

    /**
     * A [CacheBuilder] used for building a [LoadingCache] with the given expiry.
     */
    private final val cacheBuilder = CacheBuilder.newBuilder()
        .expireAfterWrite(loginAttemptProperties.expiry, loginAttemptProperties.expiryUnit)

    /**
     * A [LoadingCache] of IP addresses mapped to number of failed login attempts.
     */
    private final val ipAttemptsCache: LoadingCache<String, Int> = cacheBuilder
        .build<String, Int>(StringIntCacheLoader)

    /**
     * A [LoadingCache] of usernames mapped to number of failed login attempts.
     */
    private final val usernameAttemptsCache: LoadingCache<String, Int> = cacheBuilder
        .build(StringIntCacheLoader)

    /**
     * Catch any exceptions when getting a value from the cache and just return 0 instead.
     */
    private fun <K> LoadingCache<K, Int>.safeGet(key: K): Int =
        try {
            get(key)
        } catch (e: Exception) {
            log.error(e.message, e)
            0
        }

    fun usernameAttempts(): Map<String, Int> =
        usernameAttemptsCache.asMap()

    fun ipAttempts(): Map<String, Int> =
        ipAttemptsCache.asMap()

    fun isIPBlocked(ip: String): Boolean =
        usernameAttemptsCache.safeGet(ip) >= loginAttemptProperties.ipBlock

    fun isUsernameBlocked(username: String): Boolean =
        usernameAttemptsCache.safeGet(username) >= loginAttemptProperties.usernameBlock

    fun unblockIP(ip: String) {
        ipAttemptsCache.invalidate(ip)
    }

    fun unblockUsername(username: String) {
        usernameAttemptsCache.invalidate(username)
    }

    fun unblock(ip: String, username: String) {
        unblockIP(ip)
        unblockUsername(username)
    }

    fun loginSucceeded(ip: String, username: String) {
        unblock(ip, username)
    }

    fun loginFailed(ip: String, username: String) {
        val ipAttempts: Int = ipAttemptsCache.safeGet(ip) + 1
        val usernameAttempts: Int = usernameAttemptsCache.safeGet(username) + 1

        val msg = "Failed login attempt from username '$username' ($usernameAttempts) and IP '$ip' ($ipAttempts)"

        if (usernameAttempts >= loginAttemptProperties.usernameWarn || ipAttempts >= loginAttemptProperties.ipWarn)
            log.warn(msg)
        else
            log.debug(msg)

        ipAttemptsCache.put(ip, ipAttempts)
        usernameAttemptsCache.put(username, usernameAttempts)
    }
}
