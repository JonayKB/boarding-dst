package it.dst.garage.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NoDdbbTransactionManagerTest {
    private NoDdbbTransactionManager transactionManager;

    @BeforeEach
    protected void beforeEach() {
        transactionManager = new NoDdbbTransactionManager();
    }

    @Test
    protected void test_inTransaction_success() throws SQLException {
        assertTrue(transactionManager.inTransaction(() -> {
            return true;
        }));
    }
}
