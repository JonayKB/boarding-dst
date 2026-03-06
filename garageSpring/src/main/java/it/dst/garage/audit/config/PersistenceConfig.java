package it.dst.garage.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import it.dst.garage.audit.AuditorAwareImpl;

@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@Configuration
public class PersistenceConfig {
    
    
    @Bean("auditorProvider")
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    
}