package fr.joul.cie.api_pay.api_pay;

import fr.joul.cie.api_pay.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.ServerSocket;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private final DeploymentOptions options;

  public TestMainVerticle() throws IOException {
    var socket = new ServerSocket(0);
    Integer port = socket.getLocalPort();
    socket.close();

    options = new DeploymentOptions().setConfig(new JsonObject().put("http.server.port", port));
  }

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), options, testContext.succeedingThenComplete());
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }
}
