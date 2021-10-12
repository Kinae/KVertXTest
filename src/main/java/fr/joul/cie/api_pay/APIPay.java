package fr.joul.cie.api_pay;

import fr.joul.cie.api_pay.verticle.MainVerticle;
import io.vertx.core.Vertx;

public class APIPay {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());

  }

}
