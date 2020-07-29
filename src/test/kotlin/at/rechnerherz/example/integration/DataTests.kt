package at.rechnerherz.example.integration

import at.rechnerherz.example.base.BaseIntegrationTest
import at.rechnerherz.example.domain.account.AccountRepository
import at.rechnerherz.example.init.admin
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DataTests(
    @Autowired val accountRepository: AccountRepository
) : BaseIntegrationTest() {

    @Test
    fun `findByUsername returns the account`() {
        val admin = admin()

        accountRepository.findByUsername(admin.username) shouldBeEqualTo admin
    }

}
