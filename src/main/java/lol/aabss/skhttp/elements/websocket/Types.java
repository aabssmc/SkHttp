package lol.aabss.skhttp.elements.websocket;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.sun.net.httpserver.SimpleFileServer;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

public class Types {
    static {
        if (Classes.getExactClassInfo(WebSocket.class) == null){
            Classes.registerClass(new ClassInfo<>(WebSocket.class, "websocket")
                    .name("Websocket")
                    .description("Represents a websocket.")
                    .user("[http ]websockets?")
                    .since("1.3")
                    .parser(new Parser<>() {
                                @Override
                                public boolean canParse(@NotNull ParseContext context) {
                                    return false;
                                }
                                @Override
                                public @NotNull String toString(WebSocket o, int flags) {
                                    return toVariableNameString(o);
                                }

                                @Override
                                public @NotNull String toVariableNameString(WebSocket o) {
                                    return "websocket";
                                }
                            }
                    )
            );
        }
    }
}
