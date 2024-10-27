package com.edusn.Digizenger.Demo.utilis;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
@Component
public class DateUtil {

    public String formattedDate(LocalDateTime dateTime) {
        // Convert LocalDateTime to UTC OffsetDateTime and format to the desired pattern
        return dateTime
                .atOffset(ZoneOffset.UTC) // Convert LocalDateTime to UTC
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }
}

