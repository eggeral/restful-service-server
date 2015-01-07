package software.egger.restserver.test

import javax.inject.Inject
import javax.ws.rs.{GET, Path}


@Path("/testhk2")
class TestHk2Injection
{
  @Inject
  var messageProvider: MessageProvider = _

  @GET
  def testhk2(): String = messageProvider.getMessage()
}
