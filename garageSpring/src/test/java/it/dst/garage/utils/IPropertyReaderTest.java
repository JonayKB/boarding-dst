package it.dst.garage.utils;

import org.junit.jupiter.api.Test;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

class IPropertyReaderTest {

    @Test
    void testGetProperties_Success() {
        Properties props = IPropertyReader.getProperties();

        assertNotNull(props);
        assertFalse(props.isEmpty());
        assertEquals("h2:mem:connectiontest", props.getProperty("ddbb.url"));
    }
}
