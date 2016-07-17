package com.eightkdata.research.javapgperf;


import com.eightkdata.research.javapgperf.util.PropertiesDbConfig;
import com.eightkdata.research.javapgperf.util.QueryProcessor;
import org.postgresql.PGProperty;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

public class QueryUtil {
    private static final int FETCH_SIZE = 1_000;

    private static Connection getConnectionFromProperties() throws SQLException {
        // Parses db.properties file for database configuration
        PropertiesDbConfig propertiesDbConfig;
        try {
            propertiesDbConfig = new PropertiesDbConfig(PropertiesDbConfig.DEFAULT_PROPERTIES_FILE);
        } catch (IOException e) {
            throw new RuntimeException("DbConfig resource not found or IO Error", e);
        }

        Properties properties = new Properties();
        PGProperty.USER.set(properties, propertiesDbConfig.getDbUser());
        PGProperty.PASSWORD.set(properties, propertiesDbConfig.getDbPassword());

        // Used to fetch rows in batches from the db. Will only work if the connection uses AutoCommit
        PGProperty.DEFAULT_ROW_FETCH_SIZE.set(properties, FETCH_SIZE);

        return DriverManager.getConnection(propertiesDbConfig.getPostgresJdbcUrl(), properties);
    }

    public static void executeProcessQuery(String query, QueryProcessor processor) throws SQLException {
        try(Connection connection = getConnectionFromProperties()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                processor.processQuery(resultSet);
            }
        }
    }

    public static void executeProcessQueryNoAutocommit(String query, QueryProcessor processor) throws SQLException {
        try(Connection connection = getConnectionFromProperties()) {
            connection.setAutoCommit(false);
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                processor.processQuery(resultSet);
            }
            connection.commit();
        }
    }

    public static void executeProcessQuery(String query) throws SQLException {
        executeProcessQuery(query, resultSet -> {});
    }
}
