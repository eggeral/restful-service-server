package eggeral.restserver

import java.net.{ConnectException, InetSocketAddress}
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import javax.ws.rs.client.ClientBuilder


class RestServerSpec extends Spec
{
  "A RestServer" can "serve a Restful web services" in {
    //given
    RestServer.main(Array("--resources","eggeral.restserver.test.TestService1,eggeral.restserver.test.TestService2"))
    val client = ClientBuilder.newClient()

    //when
    val result = client.target("http://localhost:8080/test1").request().get().readEntity(classOf[String])

    //then
    result should be("hello world")
    shutdown(8005, "shutdown")
  }

  "A RestServer" can "be shut down sending a password to a TCP socket" in {
    //given
    RestServer.main(Array("--port","9005","--shutdown","abcd", "--resources", "eggeral.restserver.test.TestService1"))

    //when
    shutdown(9005, "abcd")

    //then
    Thread.sleep(100)
    val socketChannel = SocketChannel.open()
    intercept[ConnectException]{
      socketChannel.connect(new InetSocketAddress("localhost", 9005))
    }
  }

  def shutdown(port: Int, phrase: String) : Unit = {
    val socketChannel = SocketChannel.open()
    socketChannel.connect(new InetSocketAddress("localhost", port))
    socketChannel.write(ByteBuffer.wrap(phrase.getBytes()))
    socketChannel.close()
  }
}
