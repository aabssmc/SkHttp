package lol.aabss.skhttp.objects.server;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import lol.aabss.skhttp.SkHttp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpExchange {
    public HttpExchange(com.sun.net.httpserver.HttpExchange exchange, String path) {
        this.exchange = exchange;
        this.path = path;
        SkHttp.LAST_EXCHANGE = this;
    }

    private final String path;
    public com.sun.net.httpserver.HttpExchange exchange;

    public void respond(int responseCode){
        respond(null, responseCode);
    }

    public void respond(@NotNull Object response, int responseCode){
        try {
            String string = response.toString();
            if (string == null){
                exchange.sendResponseHeaders(responseCode, -1);
            } else {
                exchange.sendResponseHeaders(responseCode, string.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(string.getBytes());
                os.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object attribute(String key){
        return exchange.getAttribute(key);
    }

    public void attribute(String key, Object value){
        exchange.setAttribute(key, value);
    }

    public String protocol(){
        return exchange.getProtocol();
    }

    public String method(){
        return exchange.getRequestMethod();
    }

    public InetSocketAddress localAddress(){
        return exchange.getLocalAddress();
    }

    public InetSocketAddress remoteAddress(){
        return exchange.getRemoteAddress();
    }

    public String requestBody(){
        try {
            return new String(exchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String responseBody(){
        try {
            return new String(exchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Headers requestHeaders(){
        return exchange.getRequestHeaders();
    }

    public Headers responseHeaders(){
        return exchange.getResponseHeaders();
    }

    public URI uri(){
        return exchange.getRequestURI();
    }

    public int code(){
        return exchange.getResponseCode();
    }

    public void close(){
        exchange.close();
    }

    public String path(){
        return path;
    }

    public String fullPath(){
        return uri().getPath().replace("/"+path()+"/", "");
    }

    public Map<String, Object> parameters(){
        Map<String, Object> result = new HashMap<>();
        if (uri().getQuery() == null) {
            return result;
        }
        for (String param : uri().getQuery().split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8), getObject(URLDecoder.decode(entry[1], StandardCharsets.UTF_8)));
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private Object getObject(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ignored) {}
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException ignored) {}
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException ignored) {}
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException ignored) {}
        if (string.equalsIgnoreCase("true")){
            return true;
        } else if (string.equalsIgnoreCase("false")){
            return false;
        }
        try {
            return JsonParser.parseString(string).getAsJsonNull();
        } catch (JsonParseException | IllegalStateException ignored) {}
        try {
            return JsonParser.parseString(string).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException ignored) {}
        try {
            return JsonParser.parseString(string).getAsJsonArray();
        } catch (JsonParseException | IllegalStateException ignored) {}
        return string;
    }

}
