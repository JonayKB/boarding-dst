package it.dst.garage.model.dto.v1;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDtoV1 {
    @NotBlank(message = "Write the ID")
    private String id;

    @NotBlank(message = "Write the BRAND")
    private String brand;

    @NotBlank(message = "Write the MODEL")
    private String model;

    @Min(value = 1886, message = "Year must be after the invention of the car")
    @Max(value = 2026, message = "Year date must be previous to actual year")
    private int year;

    @NotBlank(message = "The plate should not be empty")
    @Pattern(regexp = "^[A-Z]{2,3}\\d{4}$", message = "Plate is not valid, should be like GDP1230")
    private String plate;

    @Override
    public String toString() {
        return "Car [id=" + id + ", brand=" + brand + ", model=" + model + ", year=" + year + ", plate=" + plate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CarDtoV1 other = (CarDtoV1) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}