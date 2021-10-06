package fr.joul.cie.api_pay.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class VehicleHandler implements Handler<RoutingContext> {

  private Router restAPI;

  public VehicleHandler(Vertx vertx) {
    restAPI = Router.router(vertx);
    restAPI.get("/:id").handler(this::getVehicle);
    restAPI.post("/:id/reports").handler(this::postReports);
    restAPI.post("/:id/journey").handler(this::postJourney);
  }

  public Router getRestAPI() {
    return restAPI;
  }

  @Override
  public void handle(RoutingContext ctx) {
    restAPI.handleContext(ctx);
  }

  private void getVehicle(RoutingContext ctx) {
    ctx.response().end();
    //Option A: do you business logic here
  }

  private void postReports(RoutingContext ctx) {
    //Option B: send an eventbus message, handle the message in the MainVerticle and serve the response here
  }

  private void postJourney(RoutingContext ctx) {
    //Option C: send an eventbus message, handle the message in a new Verticle and serve the response here
  }
}
