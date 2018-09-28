package uiak.exper.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

public class SImpleHttpServer extends AbstractVerticle {

    public static void main(String args[]) {
        DeploymentOptions options = new DeploymentOptions().setInstances(1).setWorkerPoolName("IMAYA WEB SERVER");
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(SImpleHttpServer.class,options, ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            }
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        vertx.createHttpServer(options).requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x! " + Thread.currentThread().getName());
        }).listen(8080, http -> {
            if (http.succeeded()) {
                startFuture.complete();
                System.out.println("HTTP server started on http://localhost:8080");
            } else {
                startFuture.fail(http.cause());
            }
        });
    }

}
