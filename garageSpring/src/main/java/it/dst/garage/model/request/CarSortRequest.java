package it.dst.garage.model.request;

import org.springframework.data.domain.Sort;

import it.dst.garage.enums.CarSortField;
import lombok.Data;

@Data
public class CarSortRequest {

    private String sortBy;
    private Boolean asc;

    public Sort toSort() {
        Sort.Direction direction = Boolean.TRUE.equals(asc)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String validatedField = CarSortField.resolve(sortBy);
        return Sort.by(direction, validatedField);
    }
}