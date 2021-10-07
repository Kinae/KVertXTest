package fr.joul.cie.api_pay.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HealthHandler implements Handler<RoutingContext> {

  public static final String PATH = "/health";

  private final Router restAPI;

  private HealthHandler(Vertx vertx) {
    restAPI = Router.router(vertx);
    restAPI.get("/").handler(HealthCheckHandler.create(vertx));

    HealthCheckHandler healthCheckHandler = HealthCheckHandler.createWithHealthChecks(HealthChecks.create(vertx));
    healthCheckHandler.register(
      "my-procedure",
      promise -> promise.complete(Status.OK()));
    healthCheckHandler.register(
      "my-procedure-with-timeout",
      2000,
      promise -> promise.complete(Status.OK()));

//    healthCheckHandler.register("database",
//      promise -> pool.getConnection(connection -> {
//        if (connection.failed()) {
//          promise.fail(connection.cause());
//        } else {
//          connection.result().close();
//          promise.complete(Status.OK());
//        }
//      }));

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
