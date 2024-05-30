import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.ActivatedJob
import io.camunda.zeebe.client.api.worker.JobClient
import io.camunda.zeebe.client.api.worker.JobHandler
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import java.net.URI
import java.time.Duration

val logger = KotlinLogging.logger {}

fun main() {
    val credentialsProvider: OAuthCredentialsProvider = OAuthCredentialsProviderBuilder()
        .authorizationServerUrl(System.getenv("ZEEBE_AUTHORIZATION_SERVER_URL"))
        .audience(System.getenv("ZEEBE_TOKEN_AUDIENCE"))
        .clientId(System.getenv("ZEEBE_CLIENT_ID"))
        .clientSecret(System.getenv("ZEEBE_CLIENT_SECRET"))
        .build()

    val client = ZeebeClient.newClientBuilder()
        .grpcAddress(URI.create(System.getenv("ZEEBE_GRPC_ADDRESS")))
        .restAddress(URI.create(System.getenv("ZEEBE_REST_ADDRESS")))
        .credentialsProvider(credentialsProvider)
        .build()

    logger.info { "Connected to: ${client.newTopologyRequest().send().join()}" }

    val variables = mapOf("key" to "value")
    val instance = client.newCreateInstanceCommand()
        .bpmnProcessId("PatricksProcess")
        .latestVersion()
        .variables(variables)
        .send()
        .join()

    logger.info { "Started process: ${instance.processInstanceKey}" }

    client.newWorker()
        .jobType("chargeCreditCard")
        .handler(CreditCardServiceHandler())
        .timeout(Duration.ofSeconds(10).toMillis())
        .open()
}

class CreditCardServiceHandler : JobHandler {

    @Throws(java.lang.Exception::class)
    override fun handle(client: JobClient, job: ActivatedJob) {
        logger.info { "Handle job: $job" }
        client.newCompleteCommand(job.key).send().join();
    }
}
