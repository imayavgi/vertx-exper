package examples.io.vertx.j9.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server(), ar -> {
      if (ar.failed()) {
        ar.cause().printStackTrace();
      }
    });
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(ctx -> {
      ctx.request().response().end("Hello World");
    });

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080, ar -> startFuture.handle(ar.mapEmpty()));
  }
}
