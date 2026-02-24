package it.dst.garage;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import it.dst.garage.view.CarView;

@SpringBootApplication
public class GarageApplication implements CommandLineRunner {

    @Autowired
    private CarView carView;
    
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(GarageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean isTest = Arrays.asList(env.getActiveProfiles()).contains("test");
        
        if (!isTest) {
            carView.mainMenu();
        }
    }
}
