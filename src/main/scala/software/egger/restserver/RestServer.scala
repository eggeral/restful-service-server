package software.egger.restserver

import java.net.{InetSocketAddress, URI}
import java.nio.channels.ServerSocketChannel
import java.util.Scanner
import java.util.logging.Logger

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.rogach.scallop._

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RestServerConf(arguments: Seq[String]) extends ScallopConf(arguments)
{
  val resources = opt[String](required = true)
  val resourceConfig = opt(default = Some("org.glassfish.jersey.server.ResourceConfig"))
  val port = opt(default = Some(8005))
  val shutdown = opt(default = Some("shutdown"))
  val uri = opt(default = Some("http://localhost:8080"))
  val properties = props[String]('P')
}

object RestServer
{
  private var server: HttpServer = _
  private val logger: Logger = Logger.getLogger("software.egger.restserver.RestServer")
  private var conf: RestServerConf = _

  def main(args: Array[String])
  {
    conf = new RestServerConf(args)
    start()
    setupShutdownPort()
  }

  def setupShutdownPort() : Unit =
  {
    Future
    {
      val shutdownPort = conf.port()
      logger.info(s"Shutdown port is: $shutdownPort.")
      val shutdownSocket = ServerSocketChannel.open()
      shutdownSocket.socket().bind(new InetSocketAddress(shutdownPort))
      var exit = false
      while (!exit)
      {
        val socket = shutdownSocket.accept()
        val scanner = new Scanner(socket)
        val phrase = scanner.nextLine()
        logger.info(s"Shutdown request received. Phrase was $phrase, should be ${conf.shutdown()}")
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
    val resourceConfigClass = conf.resourceConfig()

    logger.info(s"Using $resourceConfigClass as resource config.")
    val resourceConfig = Class.forName(resourceConfigClass).newInstance() match {
      case conf : ResourceConfig => conf
      case _ => throw new ClassCastException
    }

    resourceConfig.addProperties(conf.properties)
    for (resource <- conf.resources().split(","))
    {
      logger.info(s"Registering resource: $resource.")
      resourceConfig.registerClasses(Class.forName(resource))
    }
    server = GrizzlyHttpServerFactory.createHttpServer(URI.create(conf.uri()), resourceConfig)
    val config = server.getServerConfiguration
    config.setJmxEnabled(true)
    server.start()
  }

  def shutdown(): Unit =
  {
    server.shutdown()
  }
}
