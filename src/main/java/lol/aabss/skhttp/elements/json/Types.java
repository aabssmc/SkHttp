package lol.aabss.skhttp.elements.json;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lol.aabss.skhttp.SkHttp;
import lol.aabss.skhttp.objects.Json;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converter;
import org.skriptlang.skript.lang.converter.Converters;

public class Types {
    static {
        if (SkHttp.instance.getConfig().getBoolean("json-elements", true)) {
            if (Classes.getExactClassInfo(JsonObject.class) == null) {
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
                Converters.registerConverter(JsonObject.class, String.class, new Converter<>() {
                    @Override
                    public @Nullable String convert(JsonObject from) {
                        return new Json(from, null).toString();
                    }
                });
            }
            if (Classes.getExactClassInfo(JsonArray.class) == null) {
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
                Converters.registerConverter(JsonArray.class, String.class, new Converter<>() {
                    @Override
                    public @Nullable String convert(JsonArray from) {
                        return new Json(from, null).toString();
                    }
                });
            }
        }
    }
}
