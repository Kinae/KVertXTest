package eu.kinae.k.vertx.test.verticle;

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
        config.setAutomaticRecoveryEnabled(false);
        config.setReconnectAttempts(Integer.MAX_VALUE);
        config.setReconnectAttempts(5000);

        config.setAddresses(Arrays.asList(Address.parseAddresses("localhost:5672")));

        RabbitMQClient client = RabbitMQClient.create(vertx, config);

        client.start(asyncResult -> {
            if(asyncResult.succeeded()) {
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

        vertx.eventBus().consumer("rabbitmq.aliveness-test", message -> {
            vertx.eventBus().request("web-client.rabbitmq", null, it -> {
                if(it.failed()) {
                    message.fail(500, "Failed: " + it.cause());
                } else {
                    message.reply(null);
                }
            });
        });
    }

    @Override
    public void stop() throws Exception {
    }
}
