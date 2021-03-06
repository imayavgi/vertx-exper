package examples.io.vertx.core.net.echo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetSocket;
import examples.io.vertx.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class EchoClient extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(EchoClient.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createNetClient().connect(1234, "localhost", res -> {

      if (res.succeeded()) {
        NetSocket socket = res.result();
        socket.handler(buffer -> {
          System.out.println(Thread.currentThread().getName() + " Net client receiving: " + buffer.toString("UTF-8"));
        });

        // Now send some data
        for (int i = 0; i < 10; i++) {
          String str = "hello " + i + "\n";
          System.out.println(Thread.currentThread().getName()+ " Net client sending: " + str);
          socket.write(str);
        }
      } else {
        System.out.println("Failed to connect " + res.cause());
      }
    });
  }
}
