package it.dst.garage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import it.dst.garage.view.CarView;

@Component
@Profile("cli")
public class GarageCLIRunner implements CommandLineRunner {

    @Autowired
    private CarView carView;

    @Override
    public void run(String... args) {
        carView.mainMenu();
    }
}
