package fr.joul.cie.api_pay.api_pay;

import fr.joul.cie.api_pay.api_pay.handler.VehicleHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router restAPI = Router.router(vertx);

    restAPI.get("/healthcheck").handler(ctx -> ctx.response().end("OK"));

    restAPI.mountSubRouter("/vehicles", new VehicleHandler(vertx).getRestAPI());

    vertx.createHttpServer()
      .requestHandler(restAPI)
      .listen(8888)
      .onSuccess(it -> logger.info("HTTP server started on port {}", it.actualPort()))
      .onFailure(it -> logger.error("HTTP server failed to start", it.getCause()));

    startPromise.complete();
  }

}
