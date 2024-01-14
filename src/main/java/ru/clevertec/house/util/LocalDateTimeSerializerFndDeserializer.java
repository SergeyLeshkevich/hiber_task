package ru.clevertec.house.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author S.Leshkevich
 * @version 1.0
 *this class is for serializing and deserializing LocalDateTime objects
 * */
public class LocalDateTimeSerializerFndDeserializer implements JsonSerializer<LocalDateTime>, JsonDeserializer< LocalDateTime > {
    /**
     *this method is for deserializing LocalDateTime objects
     * */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withLocale(Locale.ENGLISH));
    }

    /**
     *this method is for serializing LocalDateTime objects
     * */
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return new JsonPrimitive(formatter.format(localDateTime));
    }

}
