package it.dst.garage.controller;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import it.dst.garage.enums.MainMenuOptions;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarDtoMapper;
import it.dst.garage.model.Car;
import it.dst.garage.model.dto.CarDto;
import it.dst.garage.proxy.CarProxy;
import it.dst.garage.seed.CarSeeder;
import it.dst.garage.utils.ValidationUtils;
import it.dst.garage.view.CarView;

@Controller
public class CarController {
    private static final String PROMPT_PLATE = "Write the PLATE: ";
    private static final String PROMPT_YEAR = "Write the YEAR: ";
    private static final String PROMPT_MODEL = "Write the MODEL: ";
    private static final String PROMPT_BRAND = "Write the BRAND: ";
    private static final String PROMPT_ID = "Write the ID: ";
    private static final String ERROR_YEAR_NUMBER = "Year should be a number";
    private static final int PAGE_SIZE = 5;
    private CarProxy carProxy;
    private CarView carView;

    private CarSeeder carSeeder;
    private ICarDtoMapper carDtoMapper;
    String plateRegex = "^[A-Z]{2,3}\\d{4}$";

    Pattern pattern = Pattern.compile(plateRegex);

    public CarController(CarProxy carProxy, CarSeeder carSeeder, ICarDtoMapper carDtoMapper) {
        this.carProxy = carProxy;
        this.carSeeder = carSeeder;
        this.carDtoMapper = carDtoMapper;
    }

    public String selectMainMenuOption(MainMenuOptions option) {
        switch (option) {
            case SHOW_CARS:
                return paginate(0, PAGE_SIZE, page -> carProxy.findAll(page, PAGE_SIZE));
            case SHOW_CARS_CONSTAINS_BRAND:
                String brand = carView.prompt("Write the BRAND to filter: ");
                return paginate(0, PAGE_SIZE, page -> carProxy.findByBrandContaining(brand, page, PAGE_SIZE));

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
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (carProxy.existsById(id));

        String brand = carView.prompt(PROMPT_BRAND);
        String model = carView.prompt(PROMPT_MODEL);
        int year;
        try {
            year = Integer.parseInt(carView.prompt(PROMPT_YEAR));
        } catch (NumberFormatException e) {
            return ERROR_YEAR_NUMBER;
        }
        String plate = carView.prompt(PROMPT_PLATE);

        CarDto car = new CarDto(id, brand, model, year, plate);
        String validationError = ValidationUtils.validate(car);

        if (validationError != null) {
            return validationError;
        }

        try {
            return carProxy.save(carDtoMapper.toModel(car))
                    ? "Car with id: " + car.getId() + " has been saved correctly"
                    : "Something happened during saving";
        } catch (UnvalidCarException e) {
            return e.getMessage();
        }
    }

    protected String updateCar() {
        String id = carView.prompt(PROMPT_ID);
        if (!carProxy.existsById(id)) {
            return "A car with this id does not exist";
        }

        String brand = carView.prompt(PROMPT_BRAND);
        String model = carView.prompt(PROMPT_MODEL);
        int year;
        try {
            year = Integer.parseInt(carView.prompt(PROMPT_YEAR));
        } catch (NumberFormatException e) {
            return ERROR_YEAR_NUMBER;
        }
        String plate = carView.prompt(PROMPT_PLATE);

        CarDto car = new CarDto(id, brand, model, year, plate);
        String validationError = ValidationUtils.validate(car);

        if (validationError != null) {
            return validationError;
        }

        try {
            return carProxy.update(carDtoMapper.toModel(car))
                    ? "Car with id: " + car.getId() + " has been updated correctly"
                    : "Something happened during updating";
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

    private <T> String paginate(int initialPage, int pageSize, Function<Integer, Page<T>> fetcher) {
        int page = initialPage;
        boolean browsing = true;

        while (browsing) {
            Page<T> dataPage = fetcher.apply(page);
            List<T> content = dataPage.getContent();

            if (content.isEmpty()) {
                return "No records found.";
            }

            if (dataPage.getTotalElements() <= pageSize) {
                return content.stream()
                        .map(Object::toString)
                        .collect(java.util.stream.Collectors.joining("\n"));
            }

            System.out.println(String.format("\n--- Page %d of %d (Total: %d) ---",
                    dataPage.getNumber() + 1, dataPage.getTotalPages(), dataPage.getTotalElements()));

            content.forEach(item -> System.out.println(item.toString()));

            System.out.println("\nNavigation: [n] Next | [p] Previous | [q] Quit to Menu");
            String action = carView.prompt("Select action: ").toLowerCase();

            switch (action) {
                case "n":
                    if (dataPage.hasNext())
                        page++;
                    else
                        System.out.println(">> You are on the last page.");
                    break;
                case "p":
                    if (dataPage.hasPrevious())
                        page--;
                    else
                        System.out.println(">> You are on the first page.");
                    break;
                case "q":
                    browsing = false;
                    break;
                default:
                    System.out.println(">> Invalid option.");
                    break;
            }
        }
        return "Returned to main menu.";
    }
}
