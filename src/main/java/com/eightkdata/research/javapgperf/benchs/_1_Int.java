package com.eightkdata.research.javapgperf.benchs;


import com.eightkdata.research.javapgperf.PgStatStatements;
import com.eightkdata.research.javapgperf.QueryUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.*;

public class _1_Int {
    public static final String QUERY = "SELECT i FROM number";

    @Benchmark
    public void test(Blackhole blackhole, PgStatStatements pgStatStatements) throws SQLException {
        pgStatStatements.setTestName(QueryBenchmarks.JMHTestNameFromClass(_1_Int.class));

        QueryUtil.executeProcessQuery(QUERY, resultSet -> {
                while (resultSet.next()) {
                    blackhole.consume(resultSet.getInt(1));
                }
        });
    }
}
