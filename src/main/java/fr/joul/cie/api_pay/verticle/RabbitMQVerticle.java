package fr.joul.cie.api_pay.verticle;

import com.rabbitmq.client.Address;
import io.vertx.core.AbstractVerticle;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

import java.util.Arrays;

public class RabbitMQVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    RabbitMQOptions config = new RabbitMQOptions();
    config.setUser("admin");
    config.setPassword("admin");
    config.setVirtualHost("api-pay");
    config.setAutomaticRecoveryEnabled(true);
    config.setReconnectAttempts(0);

    config.setAddresses(Arrays.asList(Address.parseAddresses("localhost:5672")));

    RabbitMQClient client = RabbitMQClient.create(vertx, config);

    client.start(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println("RabbitMQ successfully connected!");
      } else {
        System.out.println("Fail to connect to RabbitMQ " + asyncResult.cause().getMessage());
      }
    });

    vertx.eventBus().consumer("rabbitmq", message -> {
      if(client.isConnected()) {
        message.reply(null);
      } else {
        message.fail(500, "Client is not connected");
      }
    });
  }

  @Override
  public void stop() throws Exception {
  }
}
