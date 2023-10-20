package com.socialmedia.utils.formatters;

import com.socialmedia.config.ClockConfig;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ClockConfig.utcZone());
}
