package examples.io.vertx.core.eventbus.messagecodec;

import examples.io.vertx.core.eventbus.messagecodec.util.CustomMessage;
import examples.io.vertx.core.eventbus.messagecodec.util.CustomMessageCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import examples.io.vertx.util.Runner;

/**
 * Cluster receiver
 * @author Junbong
 */
public class ClusterReceiver extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runClusteredExample(ClusterReceiver.class);
  }

  @Override
  public void start() throws Exception {
    EventBus eventBus = getVertx().eventBus();

    // Register codec for custom message
    eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

    // Receive message
    eventBus.consumer("cluster-message-receiver", message -> {
      CustomMessage customMessage = (CustomMessage) message.body();

      System.out.println("Custom message received: "+customMessage.getSummary());

      // Replying is same as publishing
      CustomMessage replyMessage = new CustomMessage(200, "a00000002", "Message sent from cluster receiver!");
      message.reply(replyMessage);
    });
  }
}
