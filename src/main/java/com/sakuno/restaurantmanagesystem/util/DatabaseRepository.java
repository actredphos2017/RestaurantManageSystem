package com.sakuno.restaurantmanagesystem.util;

import java.io.PrintStream;
import java.sql.ResultSet;

public interface DatabaseRepository {
    boolean isAvailable();

    boolean isInitialized();

    boolean runStatement(String state, PrintStream errorOs);

    ResultSet runStatementWithQuery(String state, PrintStream errorOs);
}
