package examples.io.vertx.core.net.stream;

import io.vertx.core.AbstractVerticle;
import examples.io.vertx.util.Runner;

/*
 *  @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class BatchStreamServer extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(BatchStreamServer.class);
    }

    @Override
    public void start() throws Exception {
        vertx.createNetServer().connectHandler(socket -> {

            System.out.println("EchoServer Received SimpleWebClient Connection ");
            // Create batch stream for reading and writing
            BatchStream batchStream = new BatchStream(socket, socket);

            // Pause reading data
            batchStream.pause();

            // Register read stream handler
            batchStream.handler(batch -> {

                // Print received batch object from the client
                System.out.println("EchoServer Received : " + batch.getRaw().toString());

                // Write back batch object to the client
                batchStream.write(batch);

                // Check if write queue is full
                if (batchStream.writeQueueFull()) {

                    // Pause reading data
                    batchStream.pause();

                    // Called once write queue is ready to accept more data
                    batchStream.drainHandler(done -> {

                        // Resume reading data
                        batchStream.resume();

                    });
                }
            }).endHandler(v -> batchStream.end())
                    .exceptionHandler(t -> {
                        t.printStackTrace();
                        batchStream.end();
                    });

            // Resume reading data
            batchStream.resume();

        }).listen(1234);
        System.out.println("Batch server is now listening to port : 1234");
    }
}
