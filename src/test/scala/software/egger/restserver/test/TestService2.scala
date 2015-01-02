package software.egger.restserver.test

import javax.ws.rs.core.{Application, Context}
import javax.ws.rs.{GET, Path}

@Path("/test2")
class TestService2
{
  @Context
  var application: Application = _

  @GET
  def test2(): String = application.getProperties().get("message").toString
}
