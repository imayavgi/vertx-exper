package examples.io.vertx.core.verticle.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import examples.io.vertx.util.Runner;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An example illustrating how worker verticles can be deployed and how to interact with them.
 * <p>
 * This example prints the name of the current thread at various locations to exhibit the event loop <-> worker
 * thread switches.
 */
public class MainVerticle extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(MainVerticle.class);
    }

    @Override
    public void start() throws Exception {
        System.out.println("[Main] Running in " + Thread.currentThread().getName());
        vertx
                .deployVerticle("examples.io.vertx.core.verticle.worker.WorkerVerticle",
                        new DeploymentOptions().setWorker(true));


        AtomicLong count = new AtomicLong(10);
        long now = System.currentTimeMillis();
        System.out.println("MAIN Starting periodic on " + Thread.currentThread());
        vertx.setPeriodic(1000, id -> {
            if (count.decrementAndGet() < 0) {
                vertx.cancelTimer(id);
            }
            System.out.println("MAIN Periodic fired " + Thread.currentThread() + " after " + (System.currentTimeMillis() - now) + " ms");
        });


        long timerID = vertx.setPeriodic(5000, id -> {
            System.out.println("And every second this is printed");

            vertx.eventBus().send(
                    "sample.data",
                    "hello vert.x",
                    r -> {
                        System.out.println("[Main] Receiving reply ' " + r.result().body()
                                + "' in " + Thread.currentThread().getName());
                    }
            );
        });
    }
}
