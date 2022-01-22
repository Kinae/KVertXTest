package eu.kinae.k.vertx.test.handler;

import eu.kinae.k.vertx.test.verticle.WebClientVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthHandler implements Handler<RoutingContext> {

    public static final String PATH = "/health";
    private static final Logger logger = LoggerFactory.getLogger(HealthHandler.class);
    private final Router restAPI;

    private HealthHandler(Vertx vertx) {
        restAPI = Router.router(vertx);
        restAPI.get("/").handler(HealthCheckHandler.create(vertx));

        HealthCheckHandler healthCheckHandler = HealthCheckHandler.createWithHealthChecks(HealthChecks.create(vertx));
        healthCheckHandler.register("my-procedure", promise -> promise.complete(Status.OK()));
        healthCheckHandler.register("my-procedure-with-timeout", 2000, promise -> promise.complete(Status.OK()));

        healthCheckHandler.register("database", promise -> {
            vertx.eventBus().request("database", "healthcheck", it -> {
                if(it.failed()) {
                    promise.fail(it.cause());
                } else {
                    promise.complete(Status.OK());
                }
            });
        });

        healthCheckHandler.register("rabbitmq", promise -> {
            vertx.eventBus().request("rabbitmq", "healthcheck", it -> {
                if(it.failed()) {
                    promise.fail(it.cause());
                } else {
                    promise.complete(Status.OK());
                }
            });
        });

        healthCheckHandler.register("rabbitmq.aliveness-test.bus", promise -> {
            vertx.eventBus().request("rabbitmq.aliveness-test", "healthcheck", it -> {
                if(it.failed()) {
                    promise.fail(it.cause());
                } else {
                    promise.complete(Status.OK());
                }
            });
        });

        healthCheckHandler.register("rabbitmq.aliveness-test.local", promise -> {
            WebClientVerticle.client.get(15672, "localhost", "/api/aliveness-test/api-pay")
              .authentication(new UsernamePasswordCredentials("admin", "admin"))
              .send()
              .onSuccess(it -> {
                  if(it.statusCode() <= 300) {
                      promise.complete(Status.OK());
                  } else {
                      promise.fail(it.statusMessage());
                  }
              })
              .onFailure(it -> {
                  logger.error(it.getMessage(), it.getCause());
                  promise.fail(it.getCause());
              });
        });

        restAPI.get("/details").handler(healthCheckHandler);

    }

    public static Router create(Vertx vertx) {
        return new HealthHandler(vertx).restAPI;
    }

    @Override
    public void handle(RoutingContext ctx) {
        restAPI.handleContext(ctx);
    }

}
