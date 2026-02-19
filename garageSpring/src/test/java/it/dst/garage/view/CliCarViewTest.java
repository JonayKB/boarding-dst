package it.dst.garage.view;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.controller.CarController;
import it.dst.garage.enums.MainMenuOptions;

class CliCarViewTest {

    private static final String EXIT_OPTION = String.valueOf(MainMenuOptions.values().length);

    @Mock
    private CarController carController;

    private CliCarView cliCarView;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

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
    }

    @Test
    void test_mainMenu_exit_option() {
        provideInput(EXIT_OPTION + "\n");

        cliCarView = new CliCarView(carController);
        cliCarView.mainMenu();

        String output = testOut.toString();
        assertTrue(output.contains("Select one of the following options:"));
    }

    @Test
    void test_mainMenu_invalid_option_then_exit() {
        String input = "A\n" + MainMenuOptions.values().length + "\n";
        provideInput(input);

        cliCarView = new CliCarView(carController);
        cliCarView.mainMenu();

        String output = testOut.toString();
        assertTrue(output.contains("Option should be a number"));
    }

    @Test
    void test_mainMenu_out_of_length_option() {
        String outOfLength = String.valueOf(MainMenuOptions.values().length + 1);

        provideInput(outOfLength + "\n" + EXIT_OPTION + "\n");

        cliCarView = new CliCarView(carController);
        cliCarView.mainMenu();

        String output = testOut.toString();

        assertTrue(output.contains("There are " + MainMenuOptions.values().length + " options"));
    }

    @Test
    void test_mainMenu_valid_option() {
        provideInput(1 + "\n" + EXIT_OPTION + "\n");
        when(carController.selectMainMenuOption(any(MainMenuOptions.class))).thenReturn("VALID_OPTION");

        cliCarView = new CliCarView(carController);
        cliCarView.mainMenu();

        String output = testOut.toString();

        assertTrue(output.contains("VALID_OPTION"));
    }
}
