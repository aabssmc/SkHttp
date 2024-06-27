package lol.aabss.skhttp.objects.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ExampleHttpServer {

    public static void main(String[] args) {
        HttpServer server = new HttpServer(8000);
        server.createEndpoint("GET", "getTest",
                exchange -> {
                    JsonObject json = new JsonObject();
                    json.addProperty("response", "good");
                    json.add("params", new Gson().toJsonTree(exchange.parameters()).getAsJsonObject());
                    json.addProperty("path", exchange.fullPath());
                    exchange.respond(json, 200);
                }
        );
        server.start();

        // uri = http://localhost:8000/getTest/test/new?test=true
        // response = {"response":"good","params":{"test":true},"path":"test/new"}
    }
}