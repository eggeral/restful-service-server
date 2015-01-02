package software.egger.restserver

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.logging.Logger

import org.rogach.scallop._

class ShutdownRestServerConf(arguments: Seq[String]) extends ScallopConf(arguments)
{
  val port = opt(default = Some(8005))
  val shutdown = opt(default = Some("shutdown"))
}

object ShutdownRestServer
{
  private val logger: Logger = Logger.getLogger("software.egger.restserver.ShutdownRestServer")

  def main(args: Array[String])
  {
    val conf = new ShutdownRestServerConf(args)
    shutdownRestServer(conf.port(), conf.shutdown())
  }

  def shutdownRestServer(port: Int, phrase: String)
  {
    logger.info(s"Trying to shutdown server at port: $port using $phrase")
    val socketChannel = SocketChannel.open()
    socketChannel.connect(new InetSocketAddress("localhost", port))
    socketChannel.write(ByteBuffer.wrap(phrase.getBytes))
    socketChannel.close()
  }
}
