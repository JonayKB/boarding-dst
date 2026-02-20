package it.dst.garage.controller;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.dst.garage.enums.MainMenuOptions;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.proxy.CarProxy;
import it.dst.garage.seed.CarSeeder;
import it.dst.garage.view.CarView;

@Controller
public class CarController {
    private static final String PROMPT_PLATE = "Write the PLATE: ";
    private static final String PROMPT_YEAR = "Write the YEAR: ";
    private static final String PROMPT_MODEL = "Write the MODEL: ";
    private static final String PROMPT_BRAND = "Write the BRAND: ";
    private static final String PROMPT_ID = "Write the ID: ";
    private static final String ERROR_YEAR_NOT_NUMBER = "Year date must be previous to actual year";
    private static final String ERROR_PLATE_NOT_VALID = "Plate is not valid, should be like GDP1230";
    private static final String ERROR_PLATE_EMPTY = "The plate should not be empty";
    private static final String ERROR_YEAR_NUMBER = "Year should be a number";
    @Autowired
    private CarProxy carProxy;
    private CarView carView;

    @Autowired
    private CarSeeder carSeeder;
    String plateRegex = "^[A-Z]{2,3}\\d{4}$";

    Pattern pattern = Pattern.compile(plateRegex);

    public CarController(CarProxy carProxy, CarSeeder carSeeder) {
        this.carProxy = carProxy;
        this.carSeeder = carSeeder;
    }

    public String selectMainMenuOption(MainMenuOptions option) {
        switch (option) {
            case SHOW_CARS:
                List<Car> cars = carProxy.findAll();
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

                Car car = findCarById();
                return car != null ? car.toString() : "This car does not exist";
            case ADD_CAR:
                return saveCar();
            case UPDATE_CAR:
                return updateCar();
            case REMOVE_CAR:
                return deleteCar() ? "Succefuly deleted" : "This car wasn't on our database";
            case SEED_DATABASE:
                carSeeder.seed();
                return "Executed seeder";

            default:
                return "This is not a valid option";
        }

    }

    protected Car findCarById() {
        String carId = carView.prompt(PROMPT_ID);
        return carProxy.findById(carId);

    }

    protected String saveCar() {
        try {
            String id;
            do {
                id = UUID.randomUUID().toString();

            } while (carProxy.existsById(id));
            String brand = carView.prompt(PROMPT_BRAND);
            String model = carView.prompt(PROMPT_MODEL);
            int year = 0;
            try {
                year = Integer.parseInt(carView.prompt(PROMPT_YEAR));
                if (year > Year.now().getValue()) {
                    throw new UnvalidCarException(ERROR_YEAR_NOT_NUMBER);
                }
            } catch (NumberFormatException e) {
                return ERROR_YEAR_NUMBER;
            }
            String plate = carView.prompt(PROMPT_PLATE);
            if (plate == null || plate.isEmpty()) {
                throw new UnvalidCarException(ERROR_PLATE_EMPTY);
            }
            Matcher matcher = pattern.matcher(plate);
            if (!matcher.matches()) {
                throw new UnvalidCarException(ERROR_PLATE_NOT_VALID);

            }
            Car car = new Car(id, brand, model, year, plate);
            return carProxy.save(car) ? "Car with id:" + car.getId() + " has been saved correctly"
                    : "Something happend during saving";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    protected String updateCar() {
        try {
            String id = carView.prompt(PROMPT_ID);
            if (!carProxy.existsById(id)) {
                throw new UnvalidCarException("A car with this id does not exists");
            }
            String brand = carView.prompt(PROMPT_BRAND);
            String model = carView.prompt(PROMPT_MODEL);
            int year = 0;
            try {
                year = Integer.parseInt(carView.prompt(PROMPT_YEAR));
                if (year > Year.now().getValue()) {
                    throw new UnvalidCarException(ERROR_YEAR_NOT_NUMBER);
                }
            } catch (NumberFormatException e) {
                return ERROR_YEAR_NUMBER;

            }
            String plate = carView.prompt(PROMPT_PLATE);
            if (plate == null || plate.isEmpty()) {
                throw new UnvalidCarException(ERROR_PLATE_EMPTY);
            }
            Matcher matcher = pattern.matcher(plate);
            if (!matcher.matches()) {
                throw new UnvalidCarException(ERROR_PLATE_NOT_VALID);

            }
            Car car = new Car(id, brand, model, year, plate);
            return carProxy.update(car) ? "Car with id:" + car.getId() + " has been updated correctly"
                    : "Something happend during updating";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    protected boolean deleteCar() {
        String carId = carView.prompt(PROMPT_ID);
        return carProxy.delete(carId);

    }

    public CarView getCarView() {
        return carView;
    }

    public void setCarView(CarView carView) {
        this.carView = carView;
    }

}
