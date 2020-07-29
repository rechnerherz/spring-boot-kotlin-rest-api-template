package at.rechnerherz.example.init

import at.rechnerherz.example.domain.account.admin.Admin
import at.rechnerherz.example.domain.account.patient.Customer
import at.rechnerherz.example.domain.base.BaseProperties
import at.rechnerherz.example.domain.base.GenericRepositoryService
import at.rechnerherz.example.domain.document.DocumentService
import at.rechnerherz.example.domain.document.DocumentType
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Populates the database with data for testing.
 *
 * Runs on application startup, only if `spring.jpa.hibernate.ddl-auto` is set to `create` or `create-drop`.
 */
@Service
@Order(0)
class TestDataService(
    private val baseProperties: BaseProperties,
    private val hibernateProperties: HibernateProperties,
    private val repositories: GenericRepositoryService,
    private val documentService: DocumentService
) : ApplicationRunner {

    private val log = KotlinLogging.logger {}

    private lateinit var admin: Admin
    private lateinit var customer: Customer

    @Transactional(readOnly = false)
    override fun run(args: ApplicationArguments) {

        if (hibernateProperties.ddlAuto.startsWith("create") && baseProperties.populateTestData) {
            log.info("Populating test data ...")
            populateTestData()
            log.info("Populating test data done")
        } else {
            log.info("Skip populating data")
        }
    }

    private fun populateTestData() {
        populateAccounts()
        populateDocuments()
    }

    private fun populateAccounts() {
        admin = repositories.save(admin())
        customer = repositories.save(testCustomer())
    }

    private fun populateDocuments() {
        val document = documentService.saveResource(
            ClassPathResource("dev/sample.pdf"),
            DocumentType.PDF,
            "test-pdf"
        ).apply {
            this.owner = customer
        }
        repositories.save(document)
    }
}
