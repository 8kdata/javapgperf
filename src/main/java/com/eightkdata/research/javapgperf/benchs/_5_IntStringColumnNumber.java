package com.eightkdata.research.javapgperf.benchs;


import com.eightkdata.research.javapgperf.PgStatStatements;
import com.eightkdata.research.javapgperf.QueryUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.SQLException;

public class _5_IntStringColumnNumber {
    public static final String QUERY = "SELECT i, t FROM number";

    @Benchmark
    public void test(Blackhole blackhole, PgStatStatements pgStatStatements) throws SQLException {
        pgStatStatements.setTestName(QueryBenchmarks.JMHTestNameFromClass(_5_IntStringColumnNumber.class));

        QueryUtil.executeProcessQuery(QUERY, resultSet -> {
                while (resultSet.next()) {
                    blackhole.consume(resultSet.getInt(1));
                    blackhole.consume(resultSet.getString(2));
                }
        });
    }
}
