package examples.io.vertx.reactivex.web.client.simple;

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
            System.out.println(" Got SimpleWebClient Request ");
            req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
        });
        server.listen(8080);
    }
}
