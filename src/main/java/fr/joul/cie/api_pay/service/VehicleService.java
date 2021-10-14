package fr.joul.cie.api_pay.service;

import fr.joul.cie.api_pay.entity.Vehicle;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
@VertxGen
public interface VehicleService {

  void getByName(String name, Handler<AsyncResult<Vehicle>> handler);

  void getAll(Handler<AsyncResult<List<Vehicle>>> handler);

  void saveOne(Vehicle name);

  static VehicleService createProxy(Vertx vertx, String address) {
    return new VehicleServiceVertxEBProxy(vertx, address);
  }
}
