package eggeral.restserver

import java.net.{URI, InetSocketAddress}
import java.nio.channels.ServerSocketChannel
import java.util.Scanner
import java.util.logging.Logger

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import org.rogach.scallop._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments)
{
  val resources = opt(required = true, default = Some(""))
  val port = opt(default = Some(8005))
  val shutdown = opt(default = Some("shutdown"))
}

object RestServer
{
  private var server: HttpServer = _
  private val logger: Logger = Logger.getLogger("eggeral.restserver.RestServer")
  private var conf: Conf = _

  def main(args: Array[String])
  {
    conf = new Conf(args)

    start()
    Future
    {
      val shutdownPort = conf.port()
      logger.info(s"Shutdown port is: ${shutdownPort}.")
      val shutdownSocket = ServerSocketChannel.open()
      shutdownSocket.socket().bind(new InetSocketAddress(shutdownPort))
      var exit = false
      while (!exit)
      {
        val socket = shutdownSocket.accept()
        val scanner = new Scanner(socket)
        val phrase = scanner.nextLine()
        logger.info(s"Shutdown request received. Phrase was ${phrase}, should be ${conf.shutdown()}")
        if (phrase == conf.shutdown())
          exit = true
        socket.close()
      }
      logger.info("Starting shutdown.")
      shutdown()
      shutdownSocket.close()
      logger.info("Shutdown completed.")
    }
  }

  def start(): Unit =
  {
    val resourceConfig: ResourceConfig = new ResourceConfig()
    for (resource <- conf.resources().split(","))
    {
      logger.info(s"Registering resource: ${resource}.")
      resourceConfig.registerClasses(Class.forName(resource))
    }
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
