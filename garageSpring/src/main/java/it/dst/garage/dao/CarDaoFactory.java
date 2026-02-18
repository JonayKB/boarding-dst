package it.dst.garage.dao;

import java.sql.SQLException;

import it.dst.garage.exceptions.PersistanceTypeException;

public class CarDaoFactory {
    public CarDaoFactory() {
    }

    public CarDao create(String type) throws PersistanceTypeException, SQLException {
        switch (type) {
            case "map":
                return new CarMapDao();
            case "ddbb":
                return new CarJdbcDao();
            case "array":
                return new CarArrayDao();
            default:
                throw new PersistanceTypeException("Persistance type: '" + type + "' is invalid");
        }
    }
}
