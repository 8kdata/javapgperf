package com.eightkdata.research.javapgperf;

import com.eightkdata.research.javapgperf.benchs.QueryBenchmarks;
import org.openjdk.jmh.annotations.*;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;


@State(value = Scope.Benchmark)
public class PgStatStatements {
    private String testName;

    public void setTestName(String testName) {
        this.testName = testName;
    }

    private double getPGExecutionTime()  {
        String pgStatStatementsquery = "SELECT mean_time FROM pg_stat_statements WHERE query ='"
                + QueryBenchmarks.getQueryByTestName(testName) + "'";

        AtomicReference<Double> executionTimeReference = new AtomicReference<>();
        try {
            QueryUtil.executeProcessQuery(
                    pgStatStatementsquery,
                    resultSet -> {
                            resultSet.next();
                            executionTimeReference.set(resultSet.getDouble(1));
                    }
            );
        } catch (SQLException e) {
            e.printStackTrace();    // TODO
        }

        return executionTimeReference.get();
    }

    @Setup(value = Level.Trial)
    public void reset() throws SQLException {
        QueryUtil.executeProcessQuery("SELECT pg_stat_statements_reset()");
    }

    @TearDown(value = Level.Trial)
    public void printResults() throws SQLException {
        System.out.printf("PG:\t%s\t%.2f\n", testName, getPGExecutionTime());
        testName = null;
    }
}
