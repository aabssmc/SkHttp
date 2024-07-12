package lol.aabss.skhttp.objects.server;

import lol.aabss.skhttp.SkHttp;
import lol.aabss.skhttp.objects.WebHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class HttpServer {
    public HttpServer(int port) {
        this(port, 0);
    }

    public HttpServer(int port, int backlog) {
        try {
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), backlog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SkHttp.LAST_SERVER = this;
    }

    public HttpServer(com.sun.net.httpserver.HttpServer server){
        this.server = server;
        SkHttp.LAST_SERVER = this;
    }

    public com.sun.net.httpserver.HttpServer server;
    public List<String> registeredContexts = new ArrayList<>();

    public HttpContext createEndpoint(String method, String name, Consumer<HttpExchange> exchange){
        if (registeredContexts.contains("/"+name)){
            server.removeContext("/"+name);
            registeredContexts.remove("/"+name);
        }
        HttpContext context = new HttpContext(server.createContext("/" + name, exchange1 -> {
            if (exchange1.getRequestMethod().equals(method)) {
                exchange.accept(new HttpExchange(exchange1, name));
            } else {
                exchange1.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }));
        registeredContexts.add(context.path());
        return context;
    }

    public void createSite(String path, String files){
        File file = new File(SkHttp.instance.WEBSITE_FOLDER, files);
        if (!file.isDirectory() || !file.exists()){
            return;
        }
        server.createContext((path.startsWith("/") ? "" : "/")+path, new WebHandler(file));
    }

    public void deleteEndpoint(String name){
        if (registeredContexts.contains("/"+name)){
            server.removeContext("/"+name);
        }
    }

    public void deleteEndpoint(HttpContext context){
        if (registeredContexts.contains(context.path())){
            server.removeContext(context.path());
        }
    }

    public InetSocketAddress address(){
        return server.getAddress();
    }

    public Executor executor(){
        return server.getExecutor();
    }

    public void executor(Executor executor){
        server.setExecutor(executor);
    }

    public void start(){
        if (executor() == null){
            executor(null);
        }
        server.start();
        SkHttp.LOGGER.success("Server started on ip: "+ server.getAddress().getAddress().getHostName() + ", Port: "+server.getAddress().getPort());
    }

    public void stop(int delay){
        server.stop(delay);
    }

}
