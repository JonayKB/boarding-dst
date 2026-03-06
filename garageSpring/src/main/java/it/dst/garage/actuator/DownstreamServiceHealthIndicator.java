package it.dst.garage.actuator;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DownstreamServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            return checkDownstreamServiceHealth();
        } catch (Exception ex) {
            return new Health.Builder().down(ex).build();
        }
    }

    private Health checkDownstreamServiceHealth() {
        return new Health.Builder().up().build();
    }
}