package it.dst.garage.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JdbcTransactionManagerTest {
    @Mock
    private IConnectionProvider connectionProvider;

    private JdbcTransactionManager jdbcTransactionManager;

    @BeforeEach
    protected void beforeEach() throws SQLException {
        MockitoAnnotations.openMocks(this);
        jdbcTransactionManager = new JdbcTransactionManager(connectionProvider);
        when(connectionProvider.getConnection())
                .thenAnswer(invocation -> DriverManager.getConnection("jdbc:h2:mem:jdbctest;DB_CLOSE_DELAY=-1"));
    }

    @Test
    protected void test_inTransaction_success() throws SQLException {
        assertTrue(jdbcTransactionManager.inTransaction(() -> {
            return true;
        }));
    }

}
