package ru.clevertec.house.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author S.Leshkevich
 * @version 1.0
 * this class is intended for convenience of working with LocalDateTime and Timestamp objects
 */
@Getter
@Setter
@ToString
public class DateManager {

    /**
     * the method to get Gson object from this object
     */
    public static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializerFndDeserializer());
        return gsonBuilder.setPrettyPrinting().create();
    }

}
