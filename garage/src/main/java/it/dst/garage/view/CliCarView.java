package it.dst.garage.view;

import java.util.Scanner;

import it.dst.garage.controller.CarController;
import it.dst.garage.enums.MainMenuOptions;

public class CliCarView implements CarView {
    CarController carController;
    Scanner scanner;

    public CliCarView(CarController carController) {
        this.carController = carController;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String prompt(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public void mainMenu() {
        System.out.println("Select one of the following options:");
        MainMenuOptions[] options = MainMenuOptions.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println("- " + (i + 1) + " " + options[i].label);
        }
        int option = options.length - 1;
        try {
            option = Integer.parseInt(prompt("Select an option: ")) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Option should be a number");
            mainMenu();
        }
        if (option == options.length - 1) { // Last option always should be exit
            if (scanner != null) {
                scanner.close();
            }

        } else {
            System.out.println(carController.selectMainMenuOption(options[option], this));
            
            mainMenu();
        }

    }

}
