package it.dst.garage.seed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import it.dst.garage.dao.CarDao;
import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.model.Car;

@Service
public class CarSeeder {
    private final CarDao carDao;

    @Value("classpath:fixtures/cars.json")
    private Resource carsJsonResource;
    private final static Logger LOG = LoggerFactory.getLogger(CarSeeder.class);

    public CarSeeder(CarDaoFactory carDaoFactory) throws SQLException, PersistanceTypeException, IOException {
        this.carDao = carDaoFactory.create();
    }

    public void seed() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(carsJsonResource.getInputStream()))) {

            String content = reader.lines().collect(Collectors.joining());

            content = content.trim();
            if (content.startsWith("["))
                content = content.substring(1);
            if (content.endsWith("]"))
                content = content.substring(0, content.length() - 1);

            String[] objects = content.split("\\},[\\s]*\\{");

            for (String obj : objects) {
                String cleanObj = obj.replace("{", "").replace("}", "");

                String id = extractValue(cleanObj, "id");
                String brand = extractValue(cleanObj, "brand");
                String model = extractValue(cleanObj, "model");
                int year = Integer.parseInt(extractValue(cleanObj, "year"));
                String plate = extractValue(cleanObj, "plate");

                Car car = new Car(id, brand, model, year, plate);

                if (!carDao.existsById(id)) {
                    if (carDao.save(car)) {
                        LOG.info("Added succefuly car with id: " + car.getId());
                    } else {
                        LOG.error("Failed to add car with id: " + car.getId());
                    }

                } else {
                    LOG.error("Car with id: " + car.getId() + " already exists");

                }
            }

        } catch (IOException e) {
            LOG.error("Failed to read file: " + e.getMessage());

            return;
        }
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\"[\\s]*:[\\s]*\"?([^,\"]+)\"?";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
