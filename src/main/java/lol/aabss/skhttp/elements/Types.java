package lol.aabss.skhttp.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;

public class Types {
    static {
        if (Classes.getExactClassInfo(JsonObject.class) == null){
            Classes.registerClass(new ClassInfo<>(JsonObject.class, "jsonobject")
                    .name("Json Object")
                    .description("Represents a json object.")
                    .user("json ?objects?")
                    .since("1.3")
                    .parser(new Parser<>() {
                                @Override
                                public boolean canParse(@NotNull ParseContext context) {
                                    return false;
                                }
                                @Override
                                public @NotNull String toString(JsonObject o, int flags) {
                                    return toVariableNameString(o);
                                }

                                @Override
                                public @NotNull String toVariableNameString(JsonObject o) {
                                    return o.toString();
                                }
                            }
                    )
            );
        }

        if (Classes.getExactClassInfo(JsonArray.class) == null){
            Classes.registerClass(new ClassInfo<>(JsonArray.class, "jsonarray")
                    .name("Json Array")
                    .description("Represents a json array.")
                    .user("json ?arrays?")
                    .since("1.3")
                    .parser(new Parser<>() {
                                @Override
                                public boolean canParse(@NotNull ParseContext context) {
                                    return false;
                                }
                                @Override
                                public @NotNull String toString(JsonArray o, int flags) {
                                    return toVariableNameString(o);
                                }

                                @Override
                                public @NotNull String toVariableNameString(JsonArray o) {
                                    return o.toString();
                                }
                            }
                    )
            );
        }

        if (Classes.getExactClassInfo(JsonElement.class) == null){
            Classes.registerClass(new ClassInfo<>(JsonElement.class, "jsonelement")
                    .name("Json Element")
                    .description("Represents a json element.")
                    .user("json ?elements?")
                    .since("1.3")
                    .parser(new Parser<>() {
                                @Override
                                public boolean canParse(@NotNull ParseContext context) {
                                    return false;
                                }
                                @Override
                                public @NotNull String toString(JsonElement o, int flags) {
                                    return toVariableNameString(o);
                                }

                                @Override
                                public @NotNull String toVariableNameString(JsonElement o) {
                                    return o.toString();
                                }
                            }
                    )
            );
        }

        if (Classes.getExactClassInfo(JsonPrimitive.class) == null){
            Classes.registerClass(new ClassInfo<>(JsonPrimitive.class, "jsonprimitive")
                    .name("Json Primitive")
                    .description("Represents a json primitive.")
                    .user("json ?primitives?")
                    .since("1.3")
                    .parser(new Parser<>() {
                                @Override
                                public boolean canParse(@NotNull ParseContext context) {
                                    return false;
                                }
                                @Override
                                public @NotNull String toString(JsonPrimitive o, int flags) {
                                    return toVariableNameString(o);
                                }

                                @Override
                                public @NotNull String toVariableNameString(JsonPrimitive o) {
                                    return o.toString();
                                }
                            }
                    )
            );
        }
    }
}
