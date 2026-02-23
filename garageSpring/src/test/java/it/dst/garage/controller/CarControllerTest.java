package it.dst.garage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.enums.MainMenuOptions;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarDtoMapper;
import it.dst.garage.mapper.ICarDtoMapperImpl;
import it.dst.garage.model.Car;
import it.dst.garage.proxy.CarProxy;
import it.dst.garage.seed.CarSeeder;
import it.dst.garage.view.CarView;

public class CarControllerTest {
    private static final String INVALID_PLATE = "INVALID_PLATE";
    private static final int YEAR_ONE_YEAR_AFTER_NOW = Year.now().plusYears(1).getValue();
    private static final int TEST_YEAR = 2000;
    private static final String TEST_YEAR_STRING = "2000";

    private static final String TEST_BRAND = "TEST_BRAND";

    private static final String TEST_MODEL = "TEST_MODEL";

    private static final String TEST_PLATE = "GZL8298";

    private static final String TEST_ID = "TEST_ID";

    @Mock
    private CarProxy carProxy;

    @Mock
    private CarView carView;

    @Mock
    private CarSeeder carSeeder;

    private ICarDtoMapper carDtoMapper = new ICarDtoMapperImpl();

    private CarController carController;

    @BeforeEach
    protected void beforeEach() {
        MockitoAnnotations.openMocks(this);
        carController = new CarController(carProxy, carSeeder, carDtoMapper);
        carController.setCarView(carView);
    }

    @Test
    protected void test_findCarById_success() {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_ID);
        when(carProxy.findById(TEST_ID)).thenReturn(car);

        Car carFindById = carController.findCarById();
        assertNotNull(carFindById);
        assertEquals(car, carFindById);
    }

    @Test
    protected void test_findCarById_null() {
        when(carView.prompt(anyString())).thenReturn(TEST_ID);
        when(carProxy.findById(TEST_ID)).thenReturn(null);

        Car carFindById = carController.findCarById();
        assertNull(carFindById);
    }

    @Test
    protected void test_update_success() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.update(car)).thenReturn(true);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Car with id: " + car.getId() + " has been updated correctly");
    }

    @Test
    protected void test_update_error() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.update(car)).thenReturn(false);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Something happened during updating");
    }

    @Test
    protected void test_update_not_exists() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, INVALID_PLATE);
        when(carProxy.update(car)).thenReturn(true);
        when(carProxy.existsById(anyString())).thenReturn(false);
        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "A car with this id does not exist");
    }

    @Test
    protected void test_update_not_previous_year() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND,
                String.valueOf(YEAR_ONE_YEAR_AFTER_NOW), TEST_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Year date must be previous to actual year");
    }

    @Test
    protected void test_update_not_number_year() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND, "NOT A NUMBER", TEST_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Year should be a number");
    }

    @Test
    protected void test_update_empty_plate() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND,
                TEST_YEAR_STRING, "");
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Plate is not valid, should be like GDP1230, The plate should not be empty");
    }

    @Test
    protected void test_update_invalid_plate() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND,
                TEST_YEAR_STRING, INVALID_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.updateCar();
        assertNotNull(response);
        assertEquals(response, "Plate is not valid, should be like GDP1230");
    }

    @Test
    protected void test_save_success() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.save(any(Car.class))).thenReturn(true);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertTrue(response.endsWith("has been saved correctly"));
    }

    @Test
    protected void test_save_error() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.save(car)).thenReturn(false);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertEquals(response, "Something happened during saving");
    }

    @Test
    protected void test_save_not_previous_year() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND,
                String.valueOf(YEAR_ONE_YEAR_AFTER_NOW), TEST_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertEquals(response, "Year date must be previous to actual year");
    }

    @Test
    protected void test_save_not_number_year() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND, "NOT A NUMBER", TEST_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertEquals(response, "Year should be a number");
    }

    @Test
    protected void test_save_empty_plate() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND,
                TEST_YEAR_STRING, "");
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertEquals(response, "The plate should not be empty, Plate is not valid, should be like GDP1230");
    }

    @Test
    protected void test_save_invalid_plate() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND,
                TEST_YEAR_STRING, INVALID_PLATE);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.saveCar();
        assertNotNull(response);
        assertEquals(response, "Plate is not valid, should be like GDP1230");
    }

    @Test
    protected void test_main_menu_show_cars_empty() {
        when(carProxy.findAll()).thenReturn(new ArrayList<Car>());

        String response = carController.selectMainMenuOption(MainMenuOptions.SHOW_CARS);
        assertNotNull(response);
        assertEquals(response, "The list is empty");
    }

    @Test
    protected void test_main_menu_show_cars() {
        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE));
        when(carProxy.findAll()).thenReturn(cars);

        String response = carController.selectMainMenuOption(MainMenuOptions.SHOW_CARS);
        assertNotNull(response);
        assertThat(response, containsString("id=" + cars.get(0).getId()));
        assertThat(response, containsString("brand=" + cars.get(0).getBrand()));
        assertThat(response, containsString("model=" + cars.get(0).getModel()));
        assertThat(response, containsString("year=" + cars.get(0).getYear()));
        assertThat(response, containsString("plate=" + cars.get(0).getPlate()));
    }

    @Test
    protected void test_main_menu_show_car_success() {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carProxy.findById(anyString())).thenReturn(car);
        when(carView.prompt(anyString())).thenReturn(TEST_ID);

        String response = carController.selectMainMenuOption(MainMenuOptions.SHOW_CAR);
        assertThat(response, containsString("id=" + car.getId()));
        assertThat(response, containsString("brand=" + car.getBrand()));
        assertThat(response, containsString("model=" + car.getModel()));
        assertThat(response, containsString("year=" + car.getYear()));
        assertThat(response, containsString("plate=" + car.getPlate()));
    }

    @Test
    protected void test_main_menu_show_car_null() {
        when(carProxy.findById(anyString())).thenReturn(null);
        when(carView.prompt(anyString())).thenReturn(TEST_ID);

        String response = carController.selectMainMenuOption(MainMenuOptions.SHOW_CAR);
        assertEquals(response, "This car does not exist");
    }

    @Test
    protected void test_main_menu_save() throws UnvalidCarException {
        when(carView.prompt(anyString())).thenReturn(TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.save(any(Car.class))).thenReturn(true);
        when(carProxy.existsById(anyString())).thenReturn(false);

        String response = carController.selectMainMenuOption(MainMenuOptions.ADD_CAR);
        assertNotNull(response);
        assertTrue(response.endsWith("has been saved correctly"));
    }

    @Test
    protected void test_main_menu_update() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carView.prompt(anyString())).thenReturn(TEST_ID, TEST_MODEL, TEST_BRAND, TEST_YEAR_STRING, TEST_PLATE);
        when(carProxy.update(car)).thenReturn(true);
        when(carProxy.existsById(anyString())).thenReturn(true);

        String response = carController.selectMainMenuOption(MainMenuOptions.UPDATE_CAR);
        assertNotNull(response);
        assertEquals(response, "Car with id: " + car.getId() + " has been updated correctly");
    }

    @Test
    protected void test_main_menu_delete_error() {
        when(carProxy.delete(anyString())).thenReturn(false);

        String response = carController.selectMainMenuOption(MainMenuOptions.REMOVE_CAR);
        assertNotNull(response);
        assertEquals(response, "This car wasn't on our database");
    }

    @Test
    protected void test_main_menu_delete_success() {
        when(carProxy.delete(anyString())).thenReturn(true);
        when(carView.prompt(anyString())).thenReturn(TEST_ID);

        String response = carController.selectMainMenuOption(MainMenuOptions.REMOVE_CAR);
        assertNotNull(response);
        assertEquals(response, "Succefuly deleted");
    }

    @Test
    protected void test_main_menu_unvalid_option() {
        String response = carController.selectMainMenuOption(MainMenuOptions.EXIT);

        assertEquals(response, "This is not a valid option");

    }

    @Test
    protected void test_main_menu_carSeeder_option() {
        String response = carController.selectMainMenuOption(MainMenuOptions.SEED_DATABASE);

        assertEquals(response, "Executed seeder");

    }
}
