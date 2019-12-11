package pl.jigga.loom

import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import kotlin.random.Random

@Component
class BinClient {

    private val log = loggerFor<BinClient>()
    private val client: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(3))
            .build()

    fun getWithDelay(): String {
        val uri = "http://localhost/delay/${Random.nextInt(3)}"
        log.info("Request URI is: $uri")
        val request = HttpRequest.newBuilder(URI.create(uri))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .header("TransactionId", RequestId.get())
                .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

}