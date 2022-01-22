package eu.kinae.k.vertx.test.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebClientVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(WebClientVerticle.class);
    public static WebClient client;

    @Override
    public void start() throws Exception {
        WebClientOptions options = new WebClientOptions().setUserAgent("My-App/1.2.3");
        options.setKeepAlive(false);
        client = WebClient.create(vertx, options);

        vertx.eventBus().consumer("web-client.rabbitmq", message -> {
            client.get(15672, "localhost", "/api/aliveness-test/api-pay")
              .authentication(new UsernamePasswordCredentials("admin", "admin"))
              .send()
              .onSuccess(it -> {
                  if(it.statusCode() <= 300) {
                      message.reply(null);
                  } else {
                      logger.warn(it.statusMessage());
                      message.fail(it.statusCode(), it.statusMessage());
                  }
              })
              .onFailure(it -> {
                  logger.error(it.getMessage(), it.getCause());
                  message.fail(500, it.getMessage());
              });
        });
    }

}
