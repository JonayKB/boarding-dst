package it.dst.garage.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

public class DurationConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        String startNanoStr = MDC.get("startNano");

        if (startNanoStr == null) {
            return "system";
        }

        try {
            long startNano = Long.parseLong(startNanoStr);
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            return durationMs + "ms";
        } catch (NumberFormatException e) {
            return "0ms";
        }
    }
}