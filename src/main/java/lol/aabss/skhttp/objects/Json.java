package lol.aabss.skhttp.objects;

import ch.njol.skript.registrations.Classes;
import com.google.gson.*;
import lol.aabss.skhttp.SkHttp;

public class Json {

    public Json(JsonElement element){
        this.element = element;
    }

    public Json(){
        this(new JsonObject());
    }

    public Json(String key, Object value){
        this(new JsonObject());
        add(key, value);
    }

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public JsonElement element;

    public JsonElement add(String key, Object value){
        return add(key, value, true);
    }

    public JsonElement add(String key, Object value, boolean skript){
        if (value instanceof Boolean || value instanceof Number || value instanceof String){
            return addProperty(key, value);
        } else if (value == null){
            return addProperty(key, null);
        } else {
            if (Classes.getExactClassInfo(value.getClass()) != null && skript){
                if (element instanceof JsonObject) {
                    ((JsonObject) element).addProperty(key, Classes.toString(value));
                } else if (element instanceof JsonArray) {
                    ((JsonArray) element).add(Classes.toString(value));
                }
            } else {
                if (element instanceof JsonObject) {
                    ((JsonObject) element).add(key, gson.toJsonTree(value));
                } else if (element instanceof JsonArray) {
                    ((JsonArray) element).add(gson.toJsonTree(value));
                }
            }
        }
        return element;
    }

    public JsonElement addProperty(String key, Object value){
        if (value instanceof String){
            if (element instanceof JsonObject){
                ((JsonObject) element).addProperty(key, (String) value);
            } else if (element instanceof JsonArray){
                ((JsonArray) element).add((String) value);
            }
        } else if (value instanceof Number){
            if (element instanceof JsonObject){
                ((JsonObject) element).addProperty(key, (Number) value);
            } else if (element instanceof JsonArray){
                ((JsonArray) element).add((Number) value);
            }
        } else if (value instanceof Boolean){
            if (element instanceof JsonObject){
                ((JsonObject) element).addProperty(key, (Boolean) value);
            } else if (element instanceof JsonArray){
                ((JsonArray) element).add((Boolean) value);
            }
        } else if (value instanceof JsonElement){
            if (element instanceof JsonObject){
                ((JsonObject) element).add(key, (JsonElement) value);
            } else if (element instanceof JsonArray){
                ((JsonArray) element).add((JsonElement) value);
            }
        }
        return element;
    }

    public String toString() {
        if (SkHttp.instance.getConfig().getBoolean("pretty-print-json", true)) {
            return gson.toJson(element);
        }
        return element.toString();
    }

}
