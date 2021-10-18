package fr.joul.cie.api_pay.verticle;

import fr.joul.cie.api_pay.service.VehicleService;
import fr.joul.cie.api_pay.service.impl.VehicleServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class DatabaseVerticle extends AbstractVerticle {

  private SqlClient client;

  @Override
  public void start() throws Exception {
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("vertx")
      .setUser("adefraine");
    PoolOptions poolOptions = new PoolOptions().setMaxSize(3);

    client = PgPool.client(vertx, connectOptions, poolOptions);;

    new ServiceBinder(vertx)
      .setAddress("vehicle.service")
      .register(VehicleService.class, new VehicleServiceImpl(client));


    vertx.eventBus().consumer("database", message -> {
        client.query("SELECT 1").execute(it -> {
          if (it.failed()) {
            message.fail(500, it.toString());
          } else {
            message.reply(null);
//            message.isSend();
          }
        });
    });

  }

  @Override
  public void stop() throws Exception {
    client.close();
  }
}
