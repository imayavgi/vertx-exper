package examples.io.vertx.reactivex.http.server.echo;

import examples.io.vertx.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient();
    client.put(8080, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.handler(buf -> System.out.println(buf.toString("UTF-8")));
    }).setChunked(true).putHeader("Content-Type", "text/plain").write("hello").end();
  }
}
