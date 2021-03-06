package examples.io.vertx.core.http.simple;

import io.vertx.core.AbstractVerticle;
import examples.io.vertx.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleWebServer extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(SimpleWebServer.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
        }).listen(8080);
    }
}
