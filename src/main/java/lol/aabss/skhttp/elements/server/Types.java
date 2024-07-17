package lol.aabss.skhttp.elements.server;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import lol.aabss.skhttp.objects.server.HttpContext;
import lol.aabss.skhttp.objects.server.HttpExchange;
import lol.aabss.skhttp.objects.server.HttpHandler;
import lol.aabss.skhttp.objects.server.HttpServer;
import org.jetbrains.annotations.NotNull;


public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(HttpContext.class, "httpcontext")
                .name("Http Context")
                .description("Represents a http context.")
                .user("https? ?contexts?")
                .since("1.3")
                .parser(new Parser<>() {
                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }
                            @Override
                            public @NotNull String toString(HttpContext o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpContext o) {
                                return "http context";
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(HttpExchange.class, "httpexchange")
                .name("Http Exchange")
                .description("Represents a http exchange.")
                .user("https? ?exchanges?")
                .since("1.3")
                .parser(new Parser<>() {
                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }
                            @Override
                            public @NotNull String toString(HttpExchange o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpExchange o) {
                                return "http context";
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(HttpHandler.class, "httphandler")
                .name("Http Handler")
                .description("Represents a http handler.")
                .user("https? ?handlers?")
                .since("1.3")
                .parser(new Parser<>() {
                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }
                            @Override
                            public @NotNull String toString(HttpHandler o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpHandler o) {
                                return "http handler";
                            }
                        }
                )
        );

        Classes.registerClass(new ClassInfo<>(HttpServer.class, "httpserver")
                .name("Http Server")
                .description("Represents a http server.")
                .user("https? ?servers?")
                .since("1.3")
                .parser(new Parser<>() {
                            @Override
                            public boolean canParse(@NotNull ParseContext context) {
                                return false;
                            }
                            @Override
                            public @NotNull String toString(HttpServer o, int flags) {
                                return toVariableNameString(o);
                            }

                            @Override
                            public @NotNull String toVariableNameString(HttpServer o) {
                                return "http server";
                            }
                        }
                )
        );
    }
}
