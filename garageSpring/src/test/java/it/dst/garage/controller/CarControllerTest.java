package it.dst.garage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.service.CarService;
import it.dst.garage.view.CarView;

public class CarControllerTest {
    @Mock
    private CarService carService;

    @Mock
    private CarView carView;

    private CarController carController;

    @BeforeEach
    protected void beforeEach() {
        MockitoAnnotations.openMocks(this);
        carController = new CarController(carService);
        carController.setCarView(carView);
    }
}
