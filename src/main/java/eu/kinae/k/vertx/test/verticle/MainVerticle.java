package eu.kinae.k.vertx.test.verticle;

import eu.kinae.k.vertx.test.handler.HealthHandler;
import eu.kinae.k.vertx.test.handler.VehicleHandler;

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

        vertx.createHttpServer()
          .requestHandler(mainHandler)
          .listen(config().getInteger("http.server.port", 8080))
          .onSuccess(it -> logger.info("HTTP server started on port {}", it.actualPort()))
          .onFailure(it -> logger.error("HTTP server failed to start", it.getCause()));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
