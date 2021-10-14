package fr.joul.cie.api_pay.verticle;

import fr.joul.cie.api_pay.handler.HealthHandler;
import fr.joul.cie.api_pay.handler.VehicleHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start() throws Exception {
    Router mainHandler = Router.router(vertx);
    mainHandler.mountSubRouter(HealthHandler.PATH, HealthHandler.create(vertx));
    mainHandler.mountSubRouter(VehicleHandler.PATH, VehicleHandler.create(vertx));

    var i = config().getInteger("http.server.port");

    vertx.createHttpServer()
      .requestHandler(mainHandler)
      .listen(8080)
      .onSuccess(it -> logger.info("HTTP server started on port {}", it.actualPort()))
      .onFailure(it -> logger.error("HTTP server failed to start", it.getCause()));
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
