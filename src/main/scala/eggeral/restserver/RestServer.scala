package eggeral.restserver

import java.net.{URI, InetSocketAddress}
import java.nio.channels.ServerSocketChannel
import java.util.logging.Logger

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RestServer
{
  var server: HttpServer = _
  var logger: Logger = Logger.getLogger("eggeral.restserver.RestServer")

  def main(args: Array[String])
  {
    start()
    Future
    {
      val shutdownPort = 8005
      logger.info(s"Shutdown port is: ${shutdownPort}.")
      val shutdownSocket = ServerSocketChannel.open()
      shutdownSocket.socket().bind(new InetSocketAddress(shutdownPort))
      shutdownSocket.accept()
      logger.info("Shutdown request received.")
      shutdown()
      logger.info("Shutdown completed.")
    }
  }

  def start(): Unit =
  {
    val resourceConfig: ResourceConfig = new ResourceConfig()
    resourceConfig.registerClasses(classOf[TestService1])
    server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8080"), resourceConfig)
    val config = server.getServerConfiguration
    config.setJmxEnabled(true)
    server.start()
  }

  def shutdown(): Unit =
  {
    server.shutdown()
  }
}
