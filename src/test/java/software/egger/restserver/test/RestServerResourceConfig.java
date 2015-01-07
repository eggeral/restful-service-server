package software.egger.restserver.test;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import software.egger.restserver.test.MessageProvider;
import software.egger.restserver.test.MessageProviderFactory;


// Because Scala can not distinguish between foo(int a, String ... b) and foo(int a) we need to write this in Java.
//
//Error:(70, 20) ambiguous reference to overloaded definition,
//        both method register in class ResourceConfig of type (x$1: Any, x$2: Class[_]*)org.glassfish.jersey.server.ResourceConfig
//        and  method register in class ResourceConfig of type (x$1: Any)org.glassfish.jersey.server.ResourceConfig
//        match argument types (org.glassfish.hk2.utilities.binding.AbstractBinder)
//        resourceConfig.register(new AbstractBinder { ... })
public class RestServerResourceConfig extends ResourceConfig {

    public RestServerResourceConfig() {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(MessageProviderFactory.class).to(MessageProvider.class);
            }
        });
    }
}
