package lol.aabss.skhttp.objects;

import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.nio.file.Path;

public class RequestObject {
    public HttpRequest request;
    @Nullable
    public Path path;

    public RequestObject(HttpRequest request, @Nullable Path path){
        this.request = request;
        this.path = path;
    }

    public RequestObject[] array(){
        return new RequestObject[]{this};
    }
}
