package eu.kinae.k.vertx.test.handler;

import eu.kinae.k.vertx.test.service.VehicleService;

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
//    restAPI.get("/:id").handler(this::getVehicle); // return 500 Internal Server Error
        restAPI.get("/:id")
          .handler(this::getVehicle)
          .failureHandler(frc -> frc.response()
            .setStatusCode(frc.statusCode())
            .end(frc.failure().getMessage())); // return 500 + message
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
