package software.egger.restserver.test

import javax.ws.rs.{GET, Path}

@Path("/test1")
class TestService1
{
  @GET
  def test1(): String = "hello world"
}
