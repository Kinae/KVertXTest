package fr.joul.cie.api_pay;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class APIPay {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    ConfigRetriever.create(vertx).getConfig().onSuccess(it -> {
      DeploymentOptions options = new DeploymentOptions().setInstances(1).setConfig(it);

      vertx.deployVerticle("fr.joul.cie.api_pay.verticle.MainVerticle", options);
      vertx.deployVerticle("fr.joul.cie.api_pay.verticle.DatabaseVerticle", options);
      vertx.deployVerticle("fr.joul.cie.api_pay.verticle.RabbitMQVerticle", options);
      vertx.deployVerticle("fr.joul.cie.api_pay.verticle.WebClientVerticle", options);

//      vertx.exceptionHandler(new Handler<Throwable>() {
//        @Override
//        public void handle(Throwable event) {
//          System.err.println("Error: " + event);
//        }
//      });
    });
  }

}
