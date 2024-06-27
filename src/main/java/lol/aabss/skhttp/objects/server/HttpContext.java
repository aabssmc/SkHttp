package lol.aabss.skhttp.objects.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import lol.aabss.skhttp.SkHttp;

import java.util.List;
import java.util.Map;

public class HttpContext {

    public HttpContext(com.sun.net.httpserver.HttpContext context) {
        this.context = context;
        SkHttp.LAST_CONTEXT = this;
    }

    public com.sun.net.httpserver.HttpContext context;

    public List<Filter> filters(){
        return context.getFilters();
    }

    public Authenticator authenticator() {
        return context.getAuthenticator();
    }

    public void authenticator(Authenticator authenticator) {
        context.setAuthenticator(authenticator);
    }

    public String path() {
        return context.getPath();
    }

    public Map<String, Object> attributes(){
        return context.getAttributes();
    }

    public HttpHandler handler() {
        return new HttpHandler(context.getHandler());
    }

    public void handler(HttpHandler handler) {
        context.setHandler(handler.handler);
    }

    public HttpServer server() {
        return new HttpServer(context.getServer());
    }
}
