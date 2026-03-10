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
public class CarDtoNoIdV1{

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
        return "CarDtoNoId [brand=" + brand + ", model=" + model + ", year=" + year + ", plate=" + plate + "]";
    }


}