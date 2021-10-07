package fr.joul.cie.api_pay.api_pay.handler;

import fr.joul.cie.api_pay.api_pay.TestMainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestHealthHandler extends TestMainVerticle {

  public TestHealthHandler(Vertx vertx) throws IOException {
    super(vertx);
  }

  @Test
  void health_check(Vertx vertx, VertxTestContext testContext) throws Throwable {
    client.request(HttpMethod.GET, "/health")
      .compose(HttpClientRequest::send)
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        Assertions.assertEquals(204, response.statusCode());
        testContext.completeNow();
      })));
  }

  @Test
  void health_details_check(Vertx vertx, VertxTestContext testContext) throws Throwable {
    client.request(HttpMethod.GET, "/health/details")
      .compose(HttpClientRequest::send)
      .onComplete(testContext.succeeding(response -> testContext.verify(() -> {
        Assertions.assertEquals(200, response.statusCode());
        testContext.completeNow();
      })));
  }

}
