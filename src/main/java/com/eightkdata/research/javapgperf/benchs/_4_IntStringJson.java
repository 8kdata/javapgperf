package com.eightkdata.research.javapgperf.benchs;


import com.eightkdata.research.javapgperf.PgStatStatements;
import com.eightkdata.research.javapgperf.QueryUtil;
import com.google.gson.Gson;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.SQLException;

public class _4_IntStringJson {
    public static final String QUERY = "SELECT i, t, j FROM number";

    private class JsonElements {
        private int i;
        private String t;
    }

    @Benchmark
    public void test(Blackhole blackhole, PgStatStatements pgStatStatements) throws SQLException {
        pgStatStatements.setTestName(QueryBenchmarks.JMHTestNameFromClass(_4_IntStringJson.class));

        Gson gson = new Gson();

        QueryUtil.executeProcessQuery(QUERY, resultSet -> {
                while (resultSet.next()) {
                    blackhole.consume(resultSet.getInt("i"));
                    blackhole.consume(resultSet.getString("t"));
                    blackhole.consume(gson.fromJson(resultSet.getString("j"), JsonElements.class));
                }
        });
    }
}
