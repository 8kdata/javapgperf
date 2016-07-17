package com.eightkdata.research.javapgperf.benchs;


import com.eightkdata.research.javapgperf.PgStatStatements;
import com.eightkdata.research.javapgperf.QueryUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.SQLException;

public class _3_IntString {
    public static final String QUERY = "SELECT i, t FROM number";

    @Benchmark
    public void test(Blackhole blackhole, PgStatStatements pgStatStatements) throws SQLException {
        pgStatStatements.setTestName(QueryBenchmarks.JMHTestNameFromClass(_3_IntString.class));

        QueryUtil.executeProcessQuery(QUERY, resultSet -> {
                while (resultSet.next()) {
                    blackhole.consume(resultSet.getInt("i"));
                    blackhole.consume(resultSet.getString("t"));
                }
        });
    }
}
