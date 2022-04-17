package ru.renue.camunda

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import ru.renue.camunda.service.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@SpringBootTest
@Testcontainers
@TestPropertySource(properties = ["camunda.bpm.enabled=false"])
class ApplicationServicesTest {
    companion object {
        internal val log = LoggerFactory.getLogger(ApplicationServicesTest::class.java)

        @Container
        @JvmStatic
        internal val postgres = PostgreSQLContainer("postgres:14")

        @Container
        @JvmStatic
        internal val maildev = GenericContainer("maildev/maildev:latest")
            .withExposedPorts(1080, 1025)

        @Container
        @JvmStatic
        internal val imaginary = GenericContainer("h2non/imaginary")
            .withExposedPorts(9000)
            .withCommand("-enable-url-source")

        @DynamicPropertySource
        @JvmStatic
        internal fun properties(registry: DynamicPropertyRegistry) {
            registry.add("DB_JDBC_URL") { "jdbc:postgresql://localhost:${postgres.firstMappedPort}/test" }
            registry.add("DB_JDBC_USERNAME") { "test" }
            registry.add("DB_JDBC_PASSWORD") { "test" }
            registry.add("spring.mail.port") { maildev.getMappedPort(1025) }
            registry.add("mail-dev-api-path") { "http://localhost:${maildev.getMappedPort(1080)}" }
            registry.add("imaginary.base-path") { "http://localhost:${imaginary.getMappedPort(9000)}" }
        }
    }

    @Autowired
    private lateinit var cafactService: CafactService

    @Autowired
    private lateinit var picsumService: PicsumService

    @Autowired
    private lateinit var smtpService: SmtpService

    @Autowired
    private lateinit var mailDevTestService: MailDevTestService

    @Autowired
    private lateinit var imaginaryService: ImaginaryService

    @Test
    fun cafactServiceTest() {
        assertNotNull(cafactService.getFact())
    }

    @Test
    fun picsumServiceTest() {
        val image = picsumService.getImage()
        assertNotNull(image)
        assertTrue { image.use { it.readAllBytes() }.isNotEmpty() }
    }

    @Test
    fun smtpServiceTest() {
        val subject = RandomStringUtils.randomAlphabetic(20)
        val email = "test@test"
        smtpService.sendMessage(email, subject, "body", picsumService.getImage())
        mailDevTestService.checkEmail(email, subject)
    }

    @Test
    fun imaginaryServiceTest() {
        val image = imaginaryService.watermark(picsumService.getImageUrl(), cafactService.getFact())
        assertNotNull(image)
        assertTrue { image.use { it.readAllBytes() }.isNotEmpty() }
        assertTrue { imaginary.logs.contains("GET /watermark") }
    }
}
