package pl.jigga.loom

import org.apache.catalina.connector.Connector
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@SpringBootApplication
class LoomDemoApplication

fun main(args: Array<String>) {
	runApplication<LoomDemoApplication>(*args)
}

inline fun <reified T : Any> loggerFor(): org.slf4j.Logger = LoggerFactory.getLogger(T::class.java)

@Component
class TomcatCustomizer : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	val carrierThreadCount = 2

	override fun customize(factory: TomcatServletWebServerFactory) { /*
         * Turns out that Tomcat processes incoming requests using NioEndpoint$SocketProcessor
         * (https://github.com/apache/tomcat/blob/master/java/org/apache/tomcat/util/net/NioEndpoint.java)
         * which extends SocketProcessorBase, whose run method is synchronized.
         */
		factory.addConnectorCustomizers(TomcatConnectorCustomizer { connector: Connector ->
			connector.protocolHandler.executor = VirtualThreadExecutor(Executors.newFixedThreadPool(carrierThreadCount))
		})

		// heavy weight thread connector
		val connector = Connector()
		connector.port = 8081
		connector.protocolHandler.executor = Executors.newFixedThreadPool(carrierThreadCount)
		factory.addAdditionalTomcatConnectors(connector)
	}

}