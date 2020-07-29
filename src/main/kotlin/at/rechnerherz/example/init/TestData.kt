package at.rechnerherz.example.init

import at.rechnerherz.example.domain.account.admin.Admin
import at.rechnerherz.example.domain.account.contact.ContactData
import at.rechnerherz.example.domain.account.contact.Gender
import at.rechnerherz.example.domain.account.contact.PersonalData
import at.rechnerherz.example.domain.account.patient.Customer
import com.github.javafaker.Faker
import com.github.slugify.Slugify
import java.util.*
import kotlin.random.Random

const val SEED = 1234L

val random = Random(SEED)
val javaRandom = java.util.Random(SEED)
val faker: Faker = Faker(Locale.GERMAN, javaRandom)
val slugify: Slugify = Slugify()

const val ADMIN_USERNAME = "admin@example.com"
const val ADMIN_PASSWORD = "admin"

const val TEST_CUSTOMER_USERNAME = "customer@example.com"
const val TEST_CUSTOMER_PASSWORD = "customer"

fun safeEmail(firstName: String, lastName: String): String =
    "${slugify.slugify(firstName)}.${slugify.slugify(lastName)}@example.com"

fun safeUrl(subdomain: String): String =
    "http://www.${slugify.slugify(subdomain)}.example.com"

fun admin() =
    Admin(
        ADMIN_USERNAME,
        ADMIN_PASSWORD,
        PersonalData(
            null,
            "Admin",
            "",
            "",
            "",
            null
        ),
        ContactData()
    )

fun testCustomer() =
    Customer(
        TEST_CUSTOMER_USERNAME,
        TEST_CUSTOMER_PASSWORD,
        PersonalData(
            Gender.MALE,
            "Max",
            "Mustermann",
            "",
            "",
            null
        ),
        ContactData()
    )
