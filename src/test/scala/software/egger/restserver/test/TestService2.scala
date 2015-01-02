package software.egger.restserver.test

import javax.ws.rs.{GET, Path}

@Path("/test2")
class TestService2
{
  @GET
  def test2(): String = "how are you"
}
