package in.agrostar.ulink.clothpicker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by ayush on 23/4/17.
 */

public class GsonUtils {
    public static Gson getDateCompatibleGson() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }

        });

        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(final Date src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.getTime());
            }
        });

        Gson gson = builder.create();
        return gson;
    }

    public static <T extends Object>T getObjectFromJson(String responseBody, Class<T> classType) {
        return getDateCompatibleGson().fromJson(responseBody, classType);
    }

    public static String getJsonString(Object object) {
        return getDateCompatibleGson().toJson(object).toString();
    }

}
