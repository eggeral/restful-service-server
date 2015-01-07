package software.egger.restserver.test

import org.glassfish.hk2.api.Factory

class MessageProviderFactory extends Factory[MessageProvider]
{
  override def provide(): MessageProvider = new MessageProvider

  override def dispose(instance: MessageProvider): Unit = {

  }
}
