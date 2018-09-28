package examples.io.vertx.core.net.echo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.Pump;
import examples.io.vertx.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class EchoServer extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(EchoServer.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createNetServer().connectHandler(sock -> {
      // Create a pump
      Pump.pump(sock, sock).start();

    }).listen(1234);

    System.out.println("Echo server is now listening om " + Thread.currentThread().getName());

  }
}
