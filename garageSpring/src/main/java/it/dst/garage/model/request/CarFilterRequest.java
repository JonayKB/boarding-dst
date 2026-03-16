package it.dst.garage.model.request;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarFilterRequest {
    private String brand;
    private String model;
    private String plate;
    private Integer yearFrom;
    private Integer yearTo;

    public Map<String, Object> toFilterMap() {
        Map<String, Object> filters = new HashMap<>();
        if (brand   != null) filters.put("brand",    brand);
        if (model   != null) filters.put("model",    model);
        if (plate   != null) filters.put("plate",    plate);
        if (yearFrom != null) filters.put("yearFrom", yearFrom);
        if (yearTo  != null) filters.put("yearTo",   yearTo);
        return filters;
    }
}