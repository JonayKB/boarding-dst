package it.dst.garage.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CarMigration {
    private static final String GET_MIGRATIONS = """
            SELECT *
            FROM MIGRATIONS
            """;
    private static final String GET_UNDONE_MIGRATIONS = """
            SELECT *
            FROM MIGRATIONS
            WHERE "done_at" IS NULL
            """;
    private static final String DONE_MIGRATION = """
            UPDATE MIGRATIONS
            SET "done_at" = ?
            WHERE "name" = ?
            """;

    private static final String ADD_MIGRATION = """
            INSERT INTO MIGRATIONS
            ("name")
            VALUES
            (?)
            """;

    @Value("classpath:migrations/cars/*.sql")
    private Resource[] migrations;

    public void migrate(Connection connection) throws SQLException, IOException {
        createMigrationsTable(connection);
        addNewMigrationsToTable(connection);
        executeUndoneMigrations(connection);
    }

    private void createMigrationsTable(Connection connection) throws SQLException {
        if (!tableExists(connection, "MIGRATIONS")) {
            connection.createStatement().executeUpdate("""
                    CREATE TABLE "MIGRATIONS"(
                    "name" varchar(255) primary key,
                    "done_at" TIMESTAMP
                    )
                    """);
        }
    }

    private void addNewMigrationsToTable(Connection connection) throws SQLException {
        Set<String> existingMigrations = new HashSet<>();
        try (PreparedStatement pstmt = connection.prepareStatement(GET_MIGRATIONS)) {
            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                existingMigrations.add(results.getString(1));
            }
        }
        for (Resource migration : migrations) {
            if (!existingMigrations.contains(migration.getFilename())) {
                try (PreparedStatement pstmt = connection.prepareStatement(ADD_MIGRATION)) {
                    pstmt.setString(1, migration.getFilename());
                    pstmt.executeUpdate();
                }
            }
        }
    }

    private void executeUndoneMigrations(Connection connection) throws SQLException, IOException {
        try (Statement stmt = connection.createStatement();
                ResultSet results = stmt.executeQuery(GET_UNDONE_MIGRATIONS)) {

            List<String> undoneNames = new ArrayList<>();
            while (results.next()) {
                undoneNames.add(results.getString(1));
            }

            for (Resource resource : migrations) {
                String fileName = resource.getFilename();

                if (undoneNames.contains(fileName)) {
                    executeSqlScript(connection, resource);

                    try (PreparedStatement donePstmt = connection.prepareStatement(DONE_MIGRATION)) {
                        donePstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                        donePstmt.setString(2, fileName);

                        donePstmt.executeUpdate();
                    }
                }
            }
        }
    }

    private void executeSqlScript(Connection conn, Resource resource) throws SQLException, IOException {
        try (InputStream is = resource.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String sql = reader.lines().collect(Collectors.joining("\n"));
            try (Statement scriptStmt = conn.createStatement()) {
                scriptStmt.execute(sql);
            }
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
        return resultSet.next();
    }

}
