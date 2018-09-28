package examples.io.vertx.core.http.sharing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import examples.io.vertx.util.Runner;

/**
 * An example illustrating the server sharing and round robin. The servers are identified using an id.
 * The HTTP EchoServer Verticle is instantiated twice in the deployment options.
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    vertx.deployVerticle(
        "HttpServerVerticle",
        new DeploymentOptions().setInstances(2));
  }
}
