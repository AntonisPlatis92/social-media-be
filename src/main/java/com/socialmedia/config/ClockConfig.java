package com.socialmedia.config;

import java.time.Clock;
import java.time.ZoneId;

public class ClockConfig {

    public static Clock utcClock() {
        return Clock.systemUTC();
    }

    public static Clock withZone(ZoneId zone) {
        return Clock.system(zone);
    }

    public ZoneId getZone(Clock clock) {
        return clock.getZone();
    }
}
