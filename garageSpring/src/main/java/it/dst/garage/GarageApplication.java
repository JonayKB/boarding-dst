package it.dst.garage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.dst.garage.view.CarView;

@SpringBootApplication
public class GarageApplication implements CommandLineRunner {
	// There are better options, for example, using @ConditionalOnProperty, but is
	// requiered to not use spring helpers

	@Autowired
	private CarView carView;

	public static void main(String[] args) {

		SpringApplication.run(GarageApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		carView.mainMenu();
	}

}
