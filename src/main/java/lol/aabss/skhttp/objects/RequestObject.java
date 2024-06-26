package lol.aabss.skhttp.objects;

import org.jetbrains.annotations.Nullable;

import java.net.http.HttpRequest;
import java.nio.file.Path;

public class RequestObject {
    public HttpRequest request;
    public String type;
    @Nullable
    public Path path;

    public RequestObject(HttpRequest request, String type, @Nullable Path path){
        this.request = request;
        this.type = type;
        this.path = path;
    }

    public RequestObject[] array(){
        return new RequestObject[]{this};
    }

    public enum RequestType{
        NONE,
        OBJECT,
        STRING,
        BYTES,
        PATH,
        FILE,
        INPUTSTREAM,
        INPUTSTREAMSUPPLIER
    }
}
