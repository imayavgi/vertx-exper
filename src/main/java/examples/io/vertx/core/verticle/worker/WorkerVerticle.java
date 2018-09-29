package examples.io.vertx.core.verticle.worker;

import io.vertx.core.AbstractVerticle;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An example of worker verticle
 */
public class WorkerVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        System.out.println("[Worker] Starting in " + Thread.currentThread().getName());

        AtomicLong count = new AtomicLong(10);
        long now = System.currentTimeMillis();
        System.out.println("Starting periodic on " + Thread.currentThread());
        vertx.setPeriodic(1000, id -> {
            if (count.decrementAndGet() < 0) {
                vertx.cancelTimer(id);
            }
            System.out.println("Periodic fired " + Thread.currentThread() + " after " + (System.currentTimeMillis() - now) + " ms");
        });

        vertx.eventBus().<String>consumer("sample.data", message -> {
            System.out.println("[Worker] Consuming data in " + Thread.currentThread().getName());
            String body = message.body();
            message.reply(body.toUpperCase());
        });
    }
}
