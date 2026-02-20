package it.dst.garage.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import it.dst.garage.properties.GarageProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@ConditionalOnProperty(name = GarageProperties.PERSISTANCE_TYPE, havingValue = "ddbb")
@Primary
@AllArgsConstructor
@NoArgsConstructor
public class JdbcTransactionManager implements ITransactionManager {
    @Autowired
    IConnectionProvider connectionProvider;

    public <T> T inTransaction(Supplier<T> work) throws SQLException {
        Connection conn = null;
        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);
            connectionProvider.bindConnection(conn);
            T result = work.get();
            conn.commit();
            return result;

        } catch (Exception e) {
            conn.rollback();
        } finally {
            connectionProvider.unbindConnection();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        return null;

    }
}
