package it.dst.garage.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? Optional.of(SecurityContextHolder.getContext().getAuthentication().getName())
                : Optional.of("anonymous");
    }
}