package it.dst.garage.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.dst.garage.enums.MainMenuOptions;

@ActiveProfiles("test")
@SpringBootTest
class CliCarViewIntegrationTest {

    @Autowired
    private CliCarView cliCarView;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    private static final String SHOW_CARS_OPTION = String.valueOf(MainMenuOptions.SHOW_CARS.ordinal() + 1);
    private static final String SHOW_CAR_OPTION = String.valueOf(MainMenuOptions.SHOW_CAR.ordinal() + 1);
    private static final String SHOW_CARS_CONTAINS_BRAND_OPTION = String
            .valueOf(MainMenuOptions.SHOW_CARS_CONSTAINS_BRAND.ordinal() + 1);
    private static final String ADD_CAR_OPTION = String.valueOf(MainMenuOptions.ADD_CAR.ordinal() + 1);
    private static final String UPDATE_CAR_OPTION = String.valueOf(MainMenuOptions.UPDATE_CAR.ordinal() + 1);
    private static final String REMOVE_CAR_OPTION = String.valueOf(MainMenuOptions.REMOVE_CAR.ordinal() + 1);
    private static final String SEED_DATABASE_OPTION = String.valueOf(MainMenuOptions.SEED_DATABASE.ordinal() + 1);
    private static final String EXIT_OPTION = String.valueOf(MainMenuOptions.EXIT.ordinal() + 1);

    @BeforeEach
    void beforeEach() {

        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

    }

    @AfterEach
    void afterEach() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @AfterAll
    static void afterAll() {
        try {
            Path path = Paths.get("memory:?cache=shared");
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (Exception e) {
            System.err.println("Could not delete the file: " + e.getMessage());
        }
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        cliCarView.scanner = new java.util.Scanner(System.in);
    }

    @Test
    void test_mainMenu_exit_option() {
        provideInput(EXIT_OPTION + "\n");

        cliCarView.mainMenu();

        String output = testOut.toString();
        assertTrue(output.contains("Select one of the following options:"));
    }

    @Test
    void test_mainMenu_not_number_option() {
        provideInput("invalid\n" + EXIT_OPTION + "\n");

        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Option should be a number"));
    }

    @Test
    void test_mainMenu_out_of_range_option() {
        provideInput(EXIT_OPTION + 10 + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("There are " + MainMenuOptions.values().length + " options"));
    }

    @Test
    void test_mainMenu_show_cars_empty() {
        provideInput(SHOW_CARS_OPTION + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("No records found."));
    }

    @Test
    void test_mainMenu_show_car_not_exists() {
        provideInput(SHOW_CAR_OPTION + "\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("This car does not exist"));
    }

    @Test
    void test_mainMenu_remove_car_not_exists() {
        provideInput(REMOVE_CAR_OPTION + "\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("This car wasn't on our database"));
    }

    @Test
    void test_mainMenu_update_car_not_exists() {
        provideInput(UPDATE_CAR_OPTION + "\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("A car with this id does not exist"));
    }

    @Test
    void test_mainMenu_add_car_invalid_year() {
        provideInput(ADD_CAR_OPTION + "\nbrand\nmodel\ninvalid\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Year should be a number"));
    }

    @Test
    void test_mainMenu_add_car_invalid_plate() {
        provideInput(ADD_CAR_OPTION + "\nbrand\nmodel\n2020\ninvalid\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Plate is not valid, should be like GDP1230"));
    }

    @Test
    void test_mainMenu_add_car_success_findAll_not_empty_update_delete() {
        // Add a car
        provideInput(ADD_CAR_OPTION + "\nbrand\nmodel\n2020\nGDP1230\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("has been saved correctly"));
        testOut.reset();
        // Show cars and get the ID of the added car
        provideInput(SHOW_CARS_OPTION + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id="));
        String id = output.substring(output.indexOf("id=") + 3, output.indexOf(", brand="));
        testOut.reset();
        // Show the car by ID and check the output contains the car details
        provideInput(SHOW_CAR_OPTION + "\n" + id + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id=" + id + ", brand=brand"));
        testOut.reset();
        // Update the car and check the output contains the updated details
        provideInput(UPDATE_CAR_OPTION + "\n" + id + "\nbrand2\nmodel2\n2021\nGDP1231\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        testOut.reset();
        // Show the car by ID and check the output contains the updated car details
        provideInput(SHOW_CARS_OPTION + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id=" + id + ", brand=brand2"));
        testOut.reset();
        // Delete the car and check the output confirms deletion
        provideInput(REMOVE_CAR_OPTION + "\n" + id + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Succefuly deleted"));
        testOut.reset();
        // Show cars and check the list is empty again
        provideInput(SHOW_CARS_OPTION + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("No records found."));
    }

    @Test
    void test_999_mainMenu_seed_database() {
        provideInput(SEED_DATABASE_OPTION + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Executed seeder"));
        testOut.reset();
        // Show cars and check the list is not empty
        provideInput(SHOW_CARS_OPTION + "\n" +"q\n"+ EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id="));
    }
}