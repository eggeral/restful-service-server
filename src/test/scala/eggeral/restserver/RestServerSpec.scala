package eggeral.restserver

import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import javax.ws.rs.client.ClientBuilder


class RestServerSpec extends Spec
{
  "A RestServer" can "serve a Restful web services" in {
    //given
    RestServer.main(Array("resources=eggeral.restserver.TestService1,eggeral.restserver.test.TestService2"))
    val client = ClientBuilder.newClient()

    //when

    val result = client.target("http://localhost:8080/test1").request().get().readEntity(classOf[String])

    //then
    result should be("hello world")
    val socketChannel = SocketChannel.open()
    socketChannel.connect(new InetSocketAddress("localhost", 8005))
  }
}
