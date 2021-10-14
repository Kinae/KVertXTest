package fr.joul.cie.api_pay.verticle;

import fr.joul.cie.api_pay.service.VehicleService;
import fr.joul.cie.api_pay.service.impl.VehicleServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

public class DatabaseVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    new ServiceBinder(vertx)
      .setAddress("vehicle.service")
      .register(VehicleService.class, new VehicleServiceImpl());
  }
}
