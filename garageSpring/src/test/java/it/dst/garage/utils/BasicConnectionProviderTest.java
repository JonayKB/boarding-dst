package it.dst.garage.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BasicConnectionProviderTest {
    static private BasicConnetionProvider basicConnetionProvider;

    @BeforeEach
    void beforeEach() {
        basicConnetionProvider = new BasicConnetionProvider();
    }

    @AfterEach
    void afterEach() {
        basicConnetionProvider.unbindConnection();
        basicConnetionProvider = null;
    }

    @Test
    void test_get_connection_clean() throws SQLException {
        assertNotNull(basicConnetionProvider.getConnection());
    }

    @Test
    void test_get_connection_bind() throws SQLException {
        Connection connectionBase = basicConnetionProvider.getConnection();
        basicConnetionProvider.bindConnection(connectionBase);
        Connection connectionRetry = basicConnetionProvider.getConnection();
        assertEquals(connectionBase, connectionRetry);
    }

    @Test
    void test_get_connection_unbind() throws SQLException {
        Connection connectionBase = basicConnetionProvider.getConnection();
        basicConnetionProvider.bindConnection(connectionBase);
        Connection connectionRetry = basicConnetionProvider.getConnection();
        assertEquals(connectionBase, connectionRetry);
        basicConnetionProvider.unbindConnection();
        Connection connectionUnbinded = basicConnetionProvider.getConnection();
        assertNotEquals(connectionBase, connectionUnbinded);

    }
}
