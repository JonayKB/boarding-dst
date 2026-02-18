package it.dst.garage.dao;

import it.dst.garage.exceptions.PersistanceTypeException;

public class CarDaoFactory {
    public CarDaoFactory() {
    }

    public CarDao create(String type) throws PersistanceTypeException {
        switch (type) {
            case "map":
                return new CarMapDao();
            case "ddbb":
                return new CarJDBCDao();
            case "array":
                return new CarArrayDao();
            default:
                throw new PersistanceTypeException("Persistance type: '" + type + "' is invalid");
        }
    }
}
