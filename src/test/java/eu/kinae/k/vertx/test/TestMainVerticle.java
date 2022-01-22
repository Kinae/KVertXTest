package eu.kinae.k.vertx.test;

import eu.kinae.k.vertx.test.verticle.MainVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.ServerSocket;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

    protected final HttpClient client;
    protected final DeploymentOptions options;

    public TestMainVerticle(Vertx vertx) throws IOException {
        var socket = new ServerSocket(0);
        var port = socket.getLocalPort();
        socket.close();

        options = new DeploymentOptions().setConfig(new JsonObject().put("http.server.port", port));
        client = vertx.createHttpClient(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(port));
    }

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MainVerticle(), options, testContext.succeedingThenComplete());
    }

}
