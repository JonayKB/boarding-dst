package it.dst.garage.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import it.dst.garage.properties.GarageProperties;

@Component
public class BasicConnetionProvider implements IConnectionProvider {
    private final ThreadLocal<Connection> currentConnection = new ThreadLocal<>();

    @Override
    public Connection getConnection() throws SQLException {
        if (currentConnection.get() != null) {
            return currentConnection.get();
        }
        Properties prop = IPropertyReader.getProperties();
        String url = prop.getProperty(GarageProperties.DDBB_URL);
        String user = prop.getProperty(GarageProperties.DDBB_USER);
        String password = prop.getProperty(GarageProperties.DDBB_PASSWORD);
        Connection connection = DriverManager.getConnection("jdbc:" + url, user, password);
        return connection;
    }

    public void bindConnection(Connection conn) {
        currentConnection.set(conn);
    }

    public void unbindConnection() {
        currentConnection.remove();
    }

}
