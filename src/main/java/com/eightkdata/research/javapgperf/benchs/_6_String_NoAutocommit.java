package com.eightkdata.research.javapgperf.benchs;


import com.eightkdata.research.javapgperf.PgStatStatements;
import com.eightkdata.research.javapgperf.QueryUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.SQLException;

public class _6_String_NoAutocommit {
    public static final String QUERY = "SELECT t FROM number";

    @Benchmark
    public void test(Blackhole blackhole, PgStatStatements pgStatStatements) throws SQLException {
        pgStatStatements.setTestName(QueryBenchmarks.JMHTestNameFromClass(_6_String_NoAutocommit.class));

        QueryUtil.executeProcessQueryNoAutocommit(QUERY, resultSet -> {
                while (resultSet.next()) {
                    blackhole.consume(resultSet.getString(1));
                }
        });
    }
}
