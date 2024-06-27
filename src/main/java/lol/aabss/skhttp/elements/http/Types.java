package lol.aabss.skhttp.elements.http;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import lol.aabss.skhttp.objects.RequestObject;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(HttpClient.class, "httpclient")
                .name("Http Client")
                .description("Represents an http client.")
                .user("http ?clients?")
                .since("1.0")
                .parser(new Parser<>() {
                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }
                            @Override
                            public @NotNull String toString(HttpClient o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpClient o) {
                                return "http client";
                            }
                    }
                )
        );

        Classes.registerClass(new ClassInfo<>(RequestObject.class, "httprequest")
                .name("Http Request")
                .description("Represents an http request.")
                .user("http ?requests?")
                .since("1.0")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(RequestObject o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(RequestObject o) {
                                return o.request.uri().toString();
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(HttpResponse.class, "httpresponse")
                .name("Http Response")
                .description("Represents an http response.")
                .user("http ?requests?")
                .since("1.0")
                .parser(new Parser<>() {

                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }

                            @Override
                            public @NotNull String toString(HttpResponse o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpResponse o) {
                                return "http response";
                            }
                        }
                )
        );

    }
}
