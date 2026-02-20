package it.dst.garage.utils;

import java.sql.SQLException;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

@Service
public class NoDdbbTransactionManager implements ITransactionManager {

    public <T> T inTransaction(Supplier<T> work) throws SQLException {
        return work.get();

    }
}
