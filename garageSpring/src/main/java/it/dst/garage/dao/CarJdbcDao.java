package it.dst.garage.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.dst.garage.model.Car;
import it.dst.garage.properties.DatabaseProperties;
import it.dst.garage.utils.BeanUtil;

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
            (id,brand,model,year,plate)
            VALUES
            (?,?,?,?,?)
            """;
    private static final String UPDATE_STATEMENT = """
            UPDATE "CARS"
            SET
            "brand" = ?, "model" = ?, "year" = ?, "plate" = ?
            WHERE "id" = ?
            """;
    private static final String DELETE_STATEMENT = """
            DELETE
            FROM "CARS"
            WHERE
            "id"=?
            """;

    private Connection connection;
    private DatabaseProperties databaseProperties;

    public CarJdbcDao() throws SQLException {
        databaseProperties = BeanUtil.getBean(DatabaseProperties.class);

        this.connection = DriverManager.getConnection("jdbc:" +
                databaseProperties.getUrl(),
                databaseProperties.getUser(),
                databaseProperties.getPassword());

        applyMigrations();
    }

    public void applyMigrations() throws SQLException {
        if (!tableExists(connection, "CARS")) {
            connection.createStatement()
                    .executeUpdate("""
                                CREATE TABLE "CARS"(
                                "id" varchar(255) primary key,
                                "brand" varchar(64),
                                "model" varchar(64),
                                "year" int,
                                "plate" varchar(16)
                                )
                            """);
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
        try (PreparedStatement pstmt = connection.prepareStatement(FIND_ALL_STATEMENT)) {
            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                String carId = results.getString("id");
                String carBrand = results.getString("brand");
                String carModel = results.getString("model");
                int carYear = results.getInt("year");
                String carPlate = results.getString("plate");
                cars.add(new Car(carId, carBrand, carModel, carYear, carPlate));
            }
            return cars;

        } catch (SQLException e) {
            return cars;
        }
    }

    @Override
    public Car findById(String id) {
        try (PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_STATEMENT)) {
            pstmt.setString(1, id);
            ResultSet results = pstmt.executeQuery();
            String carId = results.getString("id");
            String carBrand = results.getString("brand");
            String carModel = results.getString("model");
            int carYear = results.getInt("year");
            String carPlate = results.getString("plate");
            return new Car(carId, carBrand, carModel, carYear, carPlate);

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean save(Car car) {
        try (PreparedStatement pstmt = connection.prepareStatement(SAVE_STATEMENT)) {
            pstmt.setString(1, car.getId());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setString(5, car.getPlate());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }

    }

    @Override
    public boolean update(Car car) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_STATEMENT)) {
            pstmt.setString(1, car.getBrand());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getYear());
            pstmt.setString(4, car.getPlate());
            pstmt.setString(5, car.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_STATEMENT)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean existsById(String id) {
        try (PreparedStatement pstmt = connection.prepareStatement(EXISTS_BY_ID_STATEMENT)) {
            pstmt.setString(1, id);
            ResultSet results = pstmt.executeQuery();
            return results.getBoolean(1);

        } catch (SQLException e) {
            return false;
        }
    }

}
