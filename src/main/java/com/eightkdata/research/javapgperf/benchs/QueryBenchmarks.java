package com.eightkdata.research.javapgperf.benchs;


import java.util.HashMap;
import java.util.Map;

public enum QueryBenchmarks {
    INT(_1_Int.class, _1_Int.QUERY),
    STRING(_2_String.class, _2_String.QUERY),
    INT_STRING(_3_IntString.class, _3_IntString.QUERY),
    INT_STRING_JSON(_4_IntStringJson.class, _4_IntStringJson.QUERY),
    INT_STRING_COLUMN_NUMBER(_5_IntStringColumnNumber.class, _5_IntStringColumnNumber.QUERY),
    STRING_NO_AUTOCOMMIT(_6_String_NoAutocommit.class, _6_String_NoAutocommit.QUERY)
    ;

    private final Class<?> clazz;
    private final String query;

    QueryBenchmarks(Class<?> clazz, String query) {
        this.clazz = clazz;
        this.query = query;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    static final Map<String,String> testNamesQueries = new HashMap<>(QueryBenchmarks.values().length);
    static {
        for(QueryBenchmarks q : values()) {
            testNamesQueries.put(JMHTestNameFromClass(q.clazz), q.query);
        }
    }

    public static String JMHTestNameFromClass(Class<?> clazz) {
        return clazz.getCanonicalName() + ".test";
    }

    public static String getQueryByTestName(String testName) {
        String query = testNamesQueries.get(testName);
        if(null == query) {
            throw new IllegalArgumentException("Unknown test name '" + testName + "'");
        }

        return query;
    }

    public static QueryBenchmarks getBySimpleTestName(String testName) {
        for(QueryBenchmarks qb : values()) {
            if(qb.getClazz().getSimpleName().equals(testName)) {
                return qb;
            }
        }

        throw new IllegalArgumentException("Invalid test name '" + testName + "'");
    }
}
