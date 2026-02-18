package it.dst.garage;

import it.dst.garage.controller.CarController;
import it.dst.garage.dao.CarArrayDao;
import it.dst.garage.dao.CarDao;
import it.dst.garage.dao.CarMapDao;
import it.dst.garage.service.CarService;
import it.dst.garage.view.CarView;
import it.dst.garage.view.CliCarView;

public class Main {
    public static void main(String[] args) {
        CarDao carDao = new CarMapDao();
        CarService carService = new CarService(carDao);
        CarController carController = new CarController(carService);
        CarView carView = new CliCarView(carController);

        carView.mainMenu();
    }
}
