package it.dst.garage.controller;

import java.time.Year;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.dst.garage.enums.MainMenuOptions;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.service.CarService;
import it.dst.garage.view.CarView;

public class CarController {
    private static final String PROMPT_PLATE = "Write the PLATE: ";
    private static final String PROMPT_YEAR = "Write the YEAR: ";
    private static final String PROMPT_MODEL = "Write the MODEL: ";
    private static final String PROMPT_BRAND = "Write the BRAND: ";
    private static final String PROMPT_ID = "Write the ID: ";
    private static final String ERROR_YEAR_NOT_NUMBER = "Year date must be previous to actual year";
    private static final String ERROR_PLATE_NOT_VALID = "Plate is not valid, should be like GDP1230";
    private static final String ERROR_PLATE_EMPTY = "The plate should be empty";
    private static final String ERROR_YEAR_NUMBER = "Year should be a number";
    private CarService carService;
    String plateRegex = "^[A-Z]{2,3}\\d{4}$";

    Pattern pattern = Pattern.compile(plateRegex);

    public CarController(CarService carService) {
        this.carService = carService;
    }

    public String selectMainMenuOption(MainMenuOptions option, CarView carViewInstance) {
        switch (option) {
            case SHOW_CARS:
                List<Car> cars = carService.findAll();
                if (cars.isEmpty()) {
                    return "The list is empty";
                }
                StringBuilder str = new StringBuilder();
                cars.stream().forEach(car -> {
                    str.append(car.toString());
                    str.append("\n");
                });
                return str.toString();
            case SHOW_CAR:

                Car car = findCarById(carViewInstance);
                return car != null ? car.toString() : "This car does not exist";
            case ADD_CAR:
                return saveCar(carViewInstance);
            case UPDATE_CAR:
                return updateCar(carViewInstance);
            case REMOVE_CAR:
                return deleteCar(carViewInstance) ? "Succefuly deleted" : "This car wasn't on our database";

            default:
                return "This is not a valid option";
        }

    }

    private Car findCarById(CarView carViewInstance) {
        String carId = carViewInstance.prompt(PROMPT_ID);
        return carService.findById(carId);

    }

    private String saveCar(CarView carViewInstance) {
        try {

            String id = carViewInstance.prompt(PROMPT_ID);
            if (carService.existsById(id)) {
                throw new UnvalidCarException("A car with the same id already exists");
            }
            String brand = carViewInstance.prompt(PROMPT_BRAND);
            String model = carViewInstance.prompt(PROMPT_MODEL);
            int year = 0;
            try {
                year = Integer.parseInt(carViewInstance.prompt(PROMPT_YEAR));
                if (year > Year.now().getValue()) {
                    throw new UnvalidCarException(ERROR_YEAR_NOT_NUMBER);
                }
            } catch (NumberFormatException e) {
                return ERROR_YEAR_NUMBER;
            }
            String plate = carViewInstance.prompt(PROMPT_PLATE);
            if (plate == null || plate.isEmpty()) {
                throw new UnvalidCarException(ERROR_PLATE_EMPTY);
            }
            Matcher matcher = pattern.matcher(plate);
            if (!matcher.matches()) {
                throw new UnvalidCarException(ERROR_PLATE_NOT_VALID);

            }
            Car car = new Car(id, brand, model, year, plate);
            return carService.save(car) ? "Car with id:" + car.getId() + " has been saved correctly"
                    : "Something happend during saving";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    private String updateCar(CarView carViewInstance) {
        try {
            String id = carViewInstance.prompt(PROMPT_ID);
            if (!carService.existsById(id)) {
                throw new UnvalidCarException("A car with this id does not exists");
            }
            String brand = carViewInstance.prompt(PROMPT_BRAND);
            String model = carViewInstance.prompt(PROMPT_MODEL);
            int year = 0;
            try {
                year = Integer.parseInt(carViewInstance.prompt(PROMPT_YEAR));
                if (year > Year.now().getValue()) {
                    throw new UnvalidCarException(ERROR_YEAR_NOT_NUMBER);
                }
            } catch (NumberFormatException e) {
                return ERROR_YEAR_NUMBER;

            }
            String plate = carViewInstance.prompt(PROMPT_PLATE);
            if (plate == null || plate.isEmpty()) {
                throw new UnvalidCarException(ERROR_PLATE_EMPTY);
            }
            Matcher matcher = pattern.matcher(plate);
            if (!matcher.matches()) {
                throw new UnvalidCarException(ERROR_PLATE_NOT_VALID);

            }
            Car car = new Car(id, brand, model, year, plate);
            return carService.update(car) ? "Car with id:" + car.getId() + " has been updated correctly"
                    : "You cannot update a car that doesn't exists";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    private boolean deleteCar(CarView carViewInstance) {
        String carId = carViewInstance.prompt(PROMPT_ID);
        return carService.delete(carId);

    }
}
