package examples.io.vertx.reactivex.http.client.unmarshalling;

import examples.io.vertx.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    server.requestStream().toFlowable().subscribe(req -> {
      req.response().putHeader("content-type", "application/json").end("{\"message\":\"Hello World\"}");
    });
    server.listen(8080);
  }
}
