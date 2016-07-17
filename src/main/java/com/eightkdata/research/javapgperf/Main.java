package com.eightkdata.research.javapgperf;


import com.eightkdata.research.javapgperf.benchs.QueryBenchmarks;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int ITERATIONS = 20;
    // Help profiling with sampling agents
    private static final int NO_FORKS_RUN_ON_THE_SAME_JVM = 0;

    public static void main(String[] args) throws RunnerException {
        if(args.length != 1 || args[0] == null || args[0].isEmpty()) {
            System.exit(1);
        }
        String testName = args[0];

        Options opt = addTestToOptionsBuilder(new OptionsBuilder(), testName)
                .forks(NO_FORKS_RUN_ON_THE_SAME_JVM)
                // We need to avoid warmup iterations as they however counts towards total Postgres time
                .warmupIterations(0)
                .measurementIterations(ITERATIONS)
                .timeUnit(TimeUnit.MILLISECONDS)
                .mode(Mode.SingleShotTime)
                .verbosity(VerboseMode.SILENT)
                .build();

        Collection<RunResult> runResults = new Runner(opt).run();

        runResults.stream().forEach(runResult ->
            System.out.printf(
                    "Java:\t%s\t%.2f\n",
                    runResult.getParams().getBenchmark(),
                    runResult.getPrimaryResult().getScore()
            )
        );
    }

    private static OptionsBuilder addTestToOptionsBuilder(OptionsBuilder builder, String testName) {
        builder.include(QueryBenchmarks.getBySimpleTestName(testName).getClazz().getSimpleName());

        return builder;
    }
}
