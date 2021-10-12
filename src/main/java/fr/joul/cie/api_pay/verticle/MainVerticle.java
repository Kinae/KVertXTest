package fr.joul.cie.api_pay.verticle;

import fr.joul.cie.api_pay.handler.HealthHandler;
import fr.joul.cie.api_pay.handler.VehicleHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

  private SqlClient client;
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start() throws Exception {
    Router mainHandler = Router.router(vertx);
    mainHandler.mountSubRouter(HealthHandler.PATH, HealthHandler.create(vertx));
    mainHandler.mountSubRouter(VehicleHandler.PATH, VehicleHandler.create(vertx));

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("vertx")
      .setUser("adefraine");

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    client = PgPool.client(vertx, connectOptions, poolOptions);
    client.query("insert into test_A(id) values ('TEST 1')")
      .execute()
      .onSuccess(ar -> ar.forEach(System.out::println))
      .onFailure(err -> System.out.println("Error: " + err.getMessage()));


    vertx.createHttpServer()
      .requestHandler(mainHandler)
      .listen(config().getInteger("http.server.port"))
      .onSuccess(it -> logger.info("HTTP server started on port {}", it.actualPort()))
      .onFailure(it -> logger.error("HTTP server failed to start", it.getCause()));
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    client.close();
  }
}
