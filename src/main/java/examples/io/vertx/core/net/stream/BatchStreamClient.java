package examples.io.vertx.core.net.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import examples.io.vertx.util.Runner;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.IntStream;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class BatchStreamClient extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(BatchStreamClient.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createNetClient().connect(1234, "localhost", ar -> {
            if (ar.succeeded()) {

                NetSocket socket = ar.result();

                // Create batch stream for reading and writing
                BatchStream batchStream = new BatchStream(socket, socket);

                // Pause reading data
                batchStream.pause();

                // Register read stream handler
                batchStream.handler(batch -> {
                    System.out.println("EchoClient Received : " + batch.getRaw().toString());
                }).endHandler(v -> batchStream.end())
                        .exceptionHandler(t -> {
                            t.printStackTrace();
                            batchStream.end();
                        });

                // Resume reading data
                batchStream.resume();

                IntStream.range(1, 10).forEach(i ->  {
                    // JsonObject
                    JsonObject jsonObject = new JsonObject()
                            .put("id", UUID.randomUUID().toString())
                            .put("name", "Message "+i)
                            .put("timestamp", Instant.now());

                    // JsonArray
                    JsonArray jsonArray = new JsonArray()
                            .add(UUID.randomUUID().toString())
                            .add("Message"+i)
                            .add(Instant.now());

                    // Buffer
                    Buffer buffer = Buffer.buffer("Vert.x is awesome!");

                    // Write to socket
                    batchStream.write(new Batch(jsonObject));
                    batchStream.write(new Batch(jsonArray));
                    batchStream.write(new Batch(buffer));
                });

            } else {
                System.out.println("Failed to connect " + ar.cause());
            }
        });
    }
}
