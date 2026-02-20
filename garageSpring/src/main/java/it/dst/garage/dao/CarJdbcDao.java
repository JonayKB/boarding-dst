package it.dst.garage.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.dst.garage.model.Car;
import it.dst.garage.utils.IConnectionProvider;

@Repository
public class CarJdbcDao implements CarDao {

    private static final String FIND_BY_ID_STATEMENT = """
            SELECT * FROM "CARS"
            WHERE "id"=?
            """;

    private static final String EXISTS_BY_ID_STATEMENT = """
            SELECT EXISTS(
            SELECT 1
            FROM "CARS"
            WHERE "id"=?);
                        """;
    private static final String FIND_ALL_STATEMENT = """
            SELECT * FROM "CARS"
            """;
    private static final String SAVE_STATEMENT = """
            INSERT INTO "CARS"
            ("id","brand","model","YEAR","plate")
            VALUES
            (?,?,?,?,?)
            """;
    private static final String UPDATE_STATEMENT = """
            UPDATE "CARS"
            SET
            "brand" = ?, "model" = ?, "YEAR" = ?, "plate" = ?
            WHERE "id" = ?
            """;
    private static final String DELETE_STATEMENT = """
            DELETE
            FROM "CARS"
            WHERE
            "id"=?
            """;
    private IConnectionProvider connectionProvider;

    public CarJdbcDao(IConnectionProvider connectionProvider) throws SQLException {
        this.connectionProvider = connectionProvider;
        applyMigrations();
    }

    public void applyMigrations() throws SQLException {
        try (Connection connection = connectionProvider.getConnection()) {
            if (!tableExists(connection, "CARS")) {
                connection.createStatement()
                        .executeUpdate("""
                                    CREATE TABLE "CARS"(
                                    "id" varchar(255) primary key,
                                    "brand" varchar(64),
                                    "model" varchar(64),
                                    "YEAR" int,
                                    "plate" varchar(16)
                                    )
                                """);
            }
        }

    }

    boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
        return resultSet.next();
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<Car>();
        try (Connection connection = connectionProvider.getConnection()) {

            try (PreparedStatement pstmt = connection.prepareStatement(FIND_ALL_STATEMENT)) {
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    String carId = results.getString("id");
                    String carBrand = results.getString("brand");
                    String carModel = results.getString("model");
                    int carYear = results.getInt("YEAR");
                    String carPlate = results.getString("plate");
                    cars.add(new Car(carId, carBrand, carModel, carYear, carPlate));
                }
                return cars;

            }
        } catch (SQLException e) {
            return cars;
        }
    }

    @Override
    public Car findById(String id) {
        try (Connection connection = connectionProvider.getConnection()) {

            try (PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_STATEMENT)) {
                pstmt.setString(1, id);
                ResultSet results = pstmt.executeQuery();
                if (results.next()) {

                    String carId = results.getString("id");
                    String carBrand = results.getString("brand");
                    String carModel = results.getString("model");
                    int carYear = results.getInt("YEAR");
                    String carPlate = results.getString("plate");
                    return new Car(carId, carBrand, carModel, carYear, carPlate);
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Car car) {
        Connection connection = null;
        try {
            connection = connectionProvider.getConnection();
            boolean isTransactional = !connection.getAutoCommit();
            try (PreparedStatement pstmt = connection.prepareStatement(SAVE_STATEMENT)) {
                pstmt.setString(1, car.getId());
                pstmt.setString(2, car.getBrand());
                pstmt.setString(3, car.getModel());
                pstmt.setInt(4, car.getYear());
                pstmt.setString(5, car.getPlate());
                return pstmt.executeUpdate() > 0;
            } finally {
                if (!isTransactional) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean update(Car car) {
        Connection connection = null;
        try {
            connection = connectionProvider.getConnection();
            boolean isTransactional = !connection.getAutoCommit();
            try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_STATEMENT)) {
                pstmt.setString(1, car.getBrand());
                pstmt.setString(2, car.getModel());
                pstmt.setInt(3, car.getYear());
                pstmt.setString(4, car.getPlate());
                pstmt.setString(5, car.getId());

                return pstmt.executeUpdate() > 0;
            } finally {
                if (!isTransactional) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        Connection connection = null;
        try {
            connection = connectionProvider.getConnection();
            boolean isTransactional = !connection.getAutoCommit();
            try (PreparedStatement pstmt = connection.prepareStatement(DELETE_STATEMENT)) {
                pstmt.setString(1, id);
                return pstmt.executeUpdate() > 0;
            } finally {
                if (!isTransactional) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean existsById(String id) {
        try (Connection connection = connectionProvider.getConnection()) {

            try (PreparedStatement pstmt = connection.prepareStatement(EXISTS_BY_ID_STATEMENT)) {
                pstmt.setString(1, id);

                ResultSet results = pstmt.executeQuery();
                if (results.next()) {
                    return results.getBoolean(1);
                }
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

}
