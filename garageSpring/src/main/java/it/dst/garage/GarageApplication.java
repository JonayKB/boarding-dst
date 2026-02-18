package it.dst.garage;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import it.dst.garage.controller.CarController;
import it.dst.garage.dao.CarDao;
import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.properties.GarageProperties;
import it.dst.garage.service.CarService;
import it.dst.garage.view.CarView;
import it.dst.garage.view.CliCarView;

@SpringBootApplication
public class GarageApplication implements CommandLineRunner {
	private CarView carView;
	// There are better options, for example, using @ConditionalOnProperty, but is
	// requiered to not use spring helpers
	@Autowired
	private ApplicationContext context;
	private final static Logger LOG = LoggerFactory.getLogger(GarageApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(GarageApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		String persistanceType = context.getEnvironment().getProperty(GarageProperties.PERSISTANCE_TYPE);
		if (persistanceType == null) {
			LOG.error(
					"""
							Persistance type is required, add it on application.properties as persistance.type
							Valid Options:
								map
								array
								ddbb
							""");
			return;
		}
		CarDao carDao;
		try {
			carDao = (new CarDaoFactory()).create(persistanceType);
		} catch (PersistanceTypeException e) {
			LOG.error(e.getMessage());
			return;
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			return;
		}
		CarService carService = new CarService(carDao);
		CarController carController = new CarController(carService);
		carView = new CliCarView(carController);
		LOG.info("Persistance Type: " + persistanceType);
		carView.mainMenu();
	}

}
