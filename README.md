# javapgperf


## Goals

The goal of this project is to measure the performance for querying a [PostgreSQL](https://www.postgresql.org) database from a Java program.
The [Java Micro/Milli/Macro Harness (aka JMH)](http://openjdk.java.net/projects/code-tools/jmh/) is used to perform the benchmarks.
At the same time, the [pg_stat_statements](https://www.postgresql.org/docs/current/static/pgstatstatements.html) extension is used to measure the query times at the Postgres level.

With these tools, some basic queries are performed on a table and the performance -both at the Java and database side- is measured. This allow us to compute the overhead incurred by the Java driver and all the network traffic.
To minimize the application overhead, the data is read and then discarded (using JMH's BlackHole, to avoid potential JVM optimizations).


## Talks

This code has been used to support the content of some technical talks delivered by [@ahachete](https://twitter.com/ahachete) on some meetups and conferences like [JUG.RU](https://jugru.timepad.ru/event/346412/) or [PGday.RU](http://pgday.ru/en/2016/papers/92). You may check the uploaded [slides](http://www.slideshare.net/8kdata/java-and-postgresql-performance-features-and-the-future) and watch a [recorded video](https://www.youtube.com/watch?v=DAxSGBWGZ1M) of these talks.


## How to run the benchmarks yourself

1. Install `pg_stat_statements` to PostgreSQL. Edit `postgresql.conf` to add `pg_stat_statements` to `shared_preload_libraries`. If in doubt, check the [documentation](https://www.postgresql.org/docs/current/static/pgstatstatements.html).

2. Connect to a PostgreSQL database and run there the `generate_data.sql` script. That will require approximately 1GB of disk space.

3. Copy `src/main/resources/db.properties.sample` file to `src/main/resources/db.properties` and edit at your convenience.

4. Compile the code with maven: `mvn package`. The compiled package will be `target/benchmarks.jar`.


If you want to simply run the benchmarks, run the helper script:

    run_benchmark.sh

This script generates output in the `results/` folder. You may of course run individual tests manually:

    java -jar target/benchmarks.jar <test_name>

Output will be written to stdout.

You may also use a Java sampling profiler to perform both normal and profile runs of the benchmark by specifying the `-profile` flag to `run_benchmark.sh` and setting up some environment variables:

    PROFILE_AGENT_PATH=/path/to/liblagent.so PROFILE_OUTPUT_FILE=traces.txt ./run_benchmark.sh -profile

where the above command will be the required command to run the benchmarks while profiling with [lightweight-java-profiler](https://github.com/dcapwell/lightweight-java-profiler). All the output will be found in the `results/` folder.


## Results

It follows the results obtained on one benchmark. The benchmark was run on a dedicated m3.large instance on AWS (with no other VMs on the same physical host), without profiling enabled:

Test | Postgres (ms) | Java (ms) | Overhead (%)
---- | -------------:| ---------:| -----------:
[_1_Int](src/main/java/com/eightkdata/research/javapgperf/benchs/_1_Int.java) | 3716.57 | 4032.06 | 8.49%
[_2_String](src/main/java/com/eightkdata/research/javapgperf/benchs/_2_String.java) | 3691.12 | 6698.53 | 81.48%
[_3_IntString](src/main/java/com/eightkdata/research/javapgperf/benchs/_3_IntString.java) | 5468.97 | 8842.70 | 61.69%
[_4_IntStringJson](src/main/java/com/eightkdata/research/javapgperf/benchs/_4_IntStringJson.java) | 8935.48 | 19751.98 | 121.05%
[_5_IntStringColumnNumber](src/main/java/com/eightkdata/research/javapgperf/benchs/_5_IntStringColumnNumber.java) | 6018.63 | 9143.73 | 51.92%
[_6_String_NoAutocommit](src/main/java/com/eightkdata/research/javapgperf/benchs/_6_String_NoAutocommit.java) | 3076.55 | 4293.38 | 39.55%

Please note that test execution times vary significantly from run to run, and thus some results may look like inconsistent. But the main takeaway is that there is an overhead, and this overhead is consistently present.

It is also important to note that by specifying an explicitd fetch size and setting noAutoCommit (see [test 6](src/main/java/com/eightkdata/research/javapgperf/benchs/_6_String_NoAutocommit.java)) the execution time improves significantly (and overhead is less) due to a much reduced GC.
