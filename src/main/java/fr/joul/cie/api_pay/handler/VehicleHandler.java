package fr.joul.cie.api_pay.handler;

import fr.joul.cie.api_pay.service.VehicleService;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class VehicleHandler implements Handler<RoutingContext> {

  public static final String PATH = "/vehicles";

  private final VehicleService vehicleService;
  private final Router restAPI;

  private VehicleHandler(Vertx vertx) {
    restAPI = Router.router(vertx);
    restAPI.get("/:id").handler(this::getVehicle);
    restAPI.post("/:id/reports").handler(this::postReports);
    restAPI.post("/:id/journey").handler(this::postJourney);

    vehicleService = VehicleService.createProxy(vertx, "vehicle.service");
  }

  public static Router create(Vertx vertx) {
    return new VehicleHandler(vertx).restAPI;
  }

  @Override
  public void handle(RoutingContext ctx) {
    restAPI.handleContext(ctx);
  }

  private void getVehicle(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    System.out.println("getVehicle : " + Thread.currentThread().getId());

    vehicleService.getAll(it -> {
      if(it.failed()) {
        ctx.fail(it.cause());
      } else {
        ctx.response().setChunked(true).send(Json.encode(it.result()));
      }
    });
  }

  private void postReports(RoutingContext ctx) {
    //Option B: send an eventbus message, handle the message in the MainVerticle and serve the response here
  }

  private void postJourney(RoutingContext ctx) {
    //Option C: send an eventbus message, handle the message in a new Verticle and serve the response here
  }
}
