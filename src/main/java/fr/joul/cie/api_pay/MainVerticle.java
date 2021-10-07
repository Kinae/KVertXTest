package fr.joul.cie.api_pay;

import fr.joul.cie.api_pay.handler.HealthHandler;
import fr.joul.cie.api_pay.handler.VehicleHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router mainHandler = Router.router(vertx);
    mainHandler.mountSubRouter(HealthHandler.PATH, HealthHandler.create(vertx));
    mainHandler.mountSubRouter(VehicleHandler.PATH, VehicleHandler.create(vertx));

    vertx.createHttpServer()
      .requestHandler(mainHandler)
      .listen(config().getInteger("http.server.port"))
      .onSuccess(it -> logger.info("HTTP server started on port {}", it.actualPort()))
      .onFailure(it -> logger.error("HTTP server failed to start", it.getCause()));

    startPromise.complete();
  }

}
