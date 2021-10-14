package fr.joul.cie.api_pay.service.impl;

import fr.joul.cie.api_pay.entity.Vehicle;
import fr.joul.cie.api_pay.service.VehicleService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.sqlclient.SqlClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class VehicleServiceImpl implements VehicleService {

  private final SqlClient client;

  public VehicleServiceImpl(SqlClient client) {
      this.client = client;
  }

  @Override
  public void getByName(String name, Handler<AsyncResult<Vehicle>> handler) {
    handler.handle(Future.succeededFuture(null));
  }

  @Override
  public void getAll(Handler<AsyncResult<List<Vehicle>>> handler) {
    client.query("SELECT * FROM test_a").execute(it ->
      handler.handle(it.map(rows ->
        StreamSupport.stream(rows.spliterator(), false)
          .map(row -> new Vehicle().name(row.getString("namee")))
          .collect(Collectors.toList()))));
  }

  @Override
  public void saveOne(Vehicle name) {
    throw new UnsupportedOperationException("Operation saveOne is unsupported");
  }
}
