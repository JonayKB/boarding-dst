package it.dst.garage.dao;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.properties.GarageProperties;
import it.dst.garage.service.CarService;
import it.dst.garage.utils.IPropertyReader;

@Service
public class CarDaoFactory {
    private final static Logger LOG = LoggerFactory.getLogger(CarService.class);

    private String persistanceType;

    public CarDaoFactory() throws PersistanceTypeException {

        this.persistanceType = IPropertyReader.getProperties().getProperty(GarageProperties.PERSISTANCE_TYPE);
        if (this.persistanceType == null) {
            LOG.error(
                    """
                            Persistance type is required, add it on application.properties as persistance.type
                            Valid Options:
                            	map
                            	array
                            	ddbb
                            """);
            throw new PersistanceTypeException(
                    "Persistance type is requiered, or is invalid, current type: " + persistanceType);
        }

        LOG.info("Persistance Type: " + persistanceType);
    }

    public CarDao create() throws PersistanceTypeException, SQLException {
        switch (persistanceType) {
            case "map":
                return new CarMapDao();
            case "ddbb":
                return new CarJdbcDao();
            case "array":
                return new CarArrayDao();
            default:
                throw new PersistanceTypeException("Persistance type: '" + persistanceType + "' is invalid");
        }
    }
}
