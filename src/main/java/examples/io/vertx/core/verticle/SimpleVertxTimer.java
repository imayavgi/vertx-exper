package examples.io.vertx.core.verticle;

import io.vertx.core.Vertx;

public class SimpleVertxTimer {
    public static void main(String args[]) {
        System.out.println(Thread.currentThread());
        Vertx vertx = Vertx.vertx();
        for (int i = 0; i < 20; i++) {
            int index = i;
            vertx.setTimer(50000, timerID -> {
                System.out.println(index + ":" + Thread.currentThread());
            });
        }
        System.out.println("Started timers ...");
    }
}
