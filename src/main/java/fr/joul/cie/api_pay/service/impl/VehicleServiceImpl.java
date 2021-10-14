package fr.joul.cie.api_pay.service.impl;

import fr.joul.cie.api_pay.entity.Vehicle;
import fr.joul.cie.api_pay.service.VehicleService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.List;

public class VehicleServiceImpl implements VehicleService {

  Vehicle a = new Vehicle().name("Mustang").style("Sport").price(10000);
  Vehicle b = new Vehicle().name("Clio").style("Potato").price(10);
  Vehicle c = new Vehicle().name("Pagani").style("Hypercar").price(10000000);

  @Override
  public void getByName(String name, Handler<AsyncResult<Vehicle>> handler) {
    handler.handle(Future.succeededFuture(a));
  }

  @Override
  public void getAll(Handler<AsyncResult<List<Vehicle>>> handler) {
    System.out.println("getAll : " + Thread.currentThread().getId());
    handler.handle(Future.succeededFuture(List.of(a, b, c)));
  }

  @Override
  public void saveOne(Vehicle name) {
    throw new UnsupportedOperationException("Operation saveOne is unsupported");
  }
}
