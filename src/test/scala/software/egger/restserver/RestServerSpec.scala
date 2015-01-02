package software.egger.restserver

import java.net.{ConnectException, InetSocketAddress}
import java.nio.channels.SocketChannel
import javax.ws.rs.client.ClientBuilder
import ShutdownRestServer.shutdownRestServer

class RestServerSpec extends Spec
{
  "A RestServer" can "serve Restful web services" in
    {
      //given
      RestServer.main(Array("--resources", "software.egger.restserver.test.TestService1,software.egger.restserver.test.TestService2"))
      Thread.sleep(100)
      val client = ClientBuilder.newClient()

      //when
      val result = client.target("http://localhost:8080/test1").request().get().readEntity(classOf[String])

      //then
      result should be("hello world")
      shutdownRestServer(8005, "shutdown")
    }

  "A RestServer" can "be shut down sending a password to a TCP socket" in
    {
      //given
      RestServer.main(Array("--port", "9005", "--shutdown", "abcd", "--resources", "software.egger.restserver.test.TestService1"))
      Thread.sleep(100)

      //when
      shutdownRestServer(9005, "abcd")

      //then
      restServerShouldBeShutdown(9005)
    }

  "A RestServer" can "be bound to a given URI" in
    {
      //given
      RestServer.main(Array("--uri", "http://localhost:8180", "--resources", "software.egger.restserver.test.TestService1"))
      Thread.sleep(100)
      val client = ClientBuilder.newClient()

      //when
      val result = client.target("http://localhost:8180/test1").request().get().readEntity(classOf[String])

      //then
      result should be("hello world")
      shutdownRestServer(8005, "shutdown")
    }

  "A RestServer" can "be shutdown using the Shutdown utility" in
    {
      //given
      RestServer.main(Array("--port", "9006", "--shutdown", "test", "--resources", "software.egger.restserver.test.TestService2"))
      Thread.sleep(100)

      //when
      ShutdownRestServer.main(Array("--port", "9006", "--shutdown", "test"))
      //then

      restServerShouldBeShutdown(9006)
    }

  "A RestServer" can "pass arguments to the Resources" in
    {
      //given
      RestServer.main(Array("--resources", "software.egger.restserver.test.TestService2" , "-Pmessage=how are you?"))
      Thread.sleep(100)
      val client = ClientBuilder.newClient()

      //when
      val result = client.target("http://localhost:8080/test2").request().get().readEntity(classOf[String])

      //then
      result should be("how are you?")
      shutdownRestServer(8005, "shutdown")
    }

  private def restServerShouldBeShutdown(port: Int): Unit =
  {
    Thread.sleep(100)
    val socketChannel = SocketChannel.open()
    intercept[ConnectException]
    {
      socketChannel.connect(new InetSocketAddress("localhost", port))
    }
  }


}
