package it.dst.garage.utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionProvider {
    public Connection getConnection() throws SQLException;
}
