package it.dst.garage.controller;

import java.util.List;

import it.dst.garage.enums.MainMenuOptions;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.service.CarService;
import it.dst.garage.view.CarView;

public class CarController {
    private CarService carService;

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
        String carId = carViewInstance.prompt("Write a the id of the car: ");
        return carService.findById(carId);

    }

    private String saveCar(CarView carViewInstance) {
        String id = carViewInstance.prompt("Write the ID: ");
        String brand = carViewInstance.prompt("Write the BRAND: ");
        String model = carViewInstance.prompt("Write the MODEL: ");
        int year = 0;
        try {
            year = Integer.parseInt(carViewInstance.prompt("Write the YEAR: "));
        } catch (NumberFormatException e) {
            return "Year should be a number";
        }
        String plate = carViewInstance.prompt("Write the PLATE: ");

        Car car = new Car(id, brand, model, year, plate);
        try {
            return carService.save(car) ? "Car with id:" + car.getId() + " has been saved correctly"
                    : "Something happend during saving";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    private String updateCar(CarView carViewInstance) {
        String id = carViewInstance.prompt("Write the ID: ");
        String brand = carViewInstance.prompt("Write the BRAND: ");
        String model = carViewInstance.prompt("Write the MODEL: ");
        int year = 0;
        try {
            year = Integer.parseInt(carViewInstance.prompt("Write the YEAR: "));
        } catch (NumberFormatException e) {
            return "Year should be a number";

        }
        String plate = carViewInstance.prompt("Write the PLATE: ");

        Car car = new Car(id, brand, model, year, plate);
        try {
            return carService.update(car) ? "Car with id:" + car.getId() + " has been updated correctly"
                    : "You cannot update a car that doesn't exists";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }

    }

    private boolean deleteCar(CarView carViewInstance) {
        String carId = carViewInstance.prompt("Write a the id of the car: ");
        return carService.delete(carId);

    }
}
