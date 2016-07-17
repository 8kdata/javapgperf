package com.eightkdata.research.javapgperf.util;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryProcessor {
    void processQuery(ResultSet resultSet) throws SQLException;
}
