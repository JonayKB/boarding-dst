package it.dst.garage.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.properties.GarageProperties;

public interface IPropertyReader {
    static public Properties getProperties() {
        Properties prop = new Properties();
        try (InputStream input = CarDaoFactory.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties is no valid");
            }

            prop.load(input);

            return prop;

        } catch (IOException ex) {
            throw new RuntimeException("Aplicattion properties does not have a valid field");
        }
    }

}
