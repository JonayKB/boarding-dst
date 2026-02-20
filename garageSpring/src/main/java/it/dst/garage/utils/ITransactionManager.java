package it.dst.garage.utils;

import java.sql.SQLException;
import java.util.function.Supplier;

public interface ITransactionManager {
    public <T> T inTransaction(Supplier<T> work) throws SQLException;
}
