package examples.io.vertx.core.http.simple;

import io.vertx.core.AbstractVerticle;
import examples.io.vertx.util.Runner;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.RequestOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleWebClient extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(SimpleWebClient.class);
    }

    @Override
    public void start() throws Exception {
        System.out.println(" Creating a new SimpleWebClient ");
        HttpClientOptions options = new HttpClientOptions().
                setProtocolVersion(HttpVersion.HTTP_2);
        HttpClient httpClient = vertx.createHttpClient();
        System.out.println(" Created a new SimpleWebClient ");

        httpClient.headNow(8080, "localhost", "/", hresp -> {
            System.out.println("Got Head response " + hresp.statusCode());
            hresp.bodyHandler(body -> {
                System.out.println("Got body data " + body.toString("ISO-8859-1"));
            });
        });
        httpClient.getNow(8080, "localhost", "/", resp -> {
            System.out.println("Got response " + resp.statusCode());
            resp.bodyHandler(body -> {
                System.out.println("Got data " + body.toString("ISO-8859-1"));
            });
        });
    }
}
