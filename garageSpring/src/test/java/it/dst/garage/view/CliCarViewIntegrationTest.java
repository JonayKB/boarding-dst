package it.dst.garage.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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

    private static final String EXIT_OPTION = String.valueOf(MainMenuOptions.values().length);
    @Autowired
    private CliCarView cliCarView;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

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
        provideInput("999\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("There are 7 options"));
    }

    @Test
    void test_mainMenu_show_cars_empty() {
        provideInput("1\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("The list is empty"));
    }

    @Test
    void test_mainMenu_show_car_not_exists() {
        provideInput("2\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("This car does not exist"));
    }

    @Test
    void test_mainMenu_remove_car_not_exists() {
        provideInput("5\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("This car wasn't on our database"));
    }

    @Test
    void test_mainMenu_update_car_not_exists() {
        provideInput("4\ntest\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("A car with this id does not exist"));
    }

    @Test
    void test_mainMenu_add_car_invalid_year() {
        provideInput("3\nbrand\nmodel\ninvalid\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Year should be a number"));
    }

    @Test
    void test_mainMenu_add_car_invalid_plate() {
        provideInput("3\nbrand\nmodel\n2020\ninvalid\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Plate is not valid, should be like GDP1230"));
    }

    @Test
    void test_mainMenu_add_car_success_findAll_not_empty_update_delete() {
        // Add a car
        provideInput("3\nbrand\nmodel\n2020\nGDP1230\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("has been saved correctly"));
        testOut.reset();
        // Show cars and get the ID of the added car
        provideInput("1\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id="));
        String id = output.substring(output.indexOf("id=") + 3, output.indexOf(", brand="));
        testOut.reset();
        // Show the car by ID and check the output contains the car details
        provideInput("2\n" + id + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id=" + id + ", brand=brand"));
        testOut.reset();
        // Update the car and check the output contains the updated details
        provideInput("4\n" + id + "\nbrand2\nmodel2\n2021\nGDP1231\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        testOut.reset();
        // Show the car by ID and check the output contains the updated car details
        provideInput("1\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id=" + id + ", brand=brand2"));
        testOut.reset();
        // Delete the car and check the output confirms deletion
        provideInput("5\n" + id + "\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Succefuly deleted"));
        testOut.reset();
        // Show cars and check the list is empty again
        provideInput("1\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("The list is empty"));
    }

    @Test
    void test_999_mainMenu_seed_database() {
        provideInput("6\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        String output = testOut.toString();
        assertTrue(output.contains("Executed seeder"));
        testOut.reset();
        // Show cars and check the list is not empty
        provideInput("1\n" + EXIT_OPTION + "\n");
        cliCarView.mainMenu();
        output = testOut.toString();
        assertTrue(output.contains("Car [id="));
    }
}