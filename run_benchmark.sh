#!/bin/sh


exit_with_error() {
	echo $1"\n" >&2
	exit 1
}


[ -f target/benchmarks.jar ] || exit_with_error "Please build first with 'mvn package'"

profile=false
if [ $# -eq 1 ] && [ $1 = "-profile" ]
then
	profile=true
	[ $PROFILE_AGENT_PATH ] && [ -f $PROFILE_AGENT_PATH ] \
		|| exit_with_error "If you use the flag -profile you must set the profile agent path to env variable PROFILE_AGENT_PATH"
	PROFILE_OPTS="-XX:+PreserveFramePointer -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -agentpath:$PROFILE_AGENT_PATH"
	[ $PROFILE_OUTPUT_FILE ] \
		|| exit_with_error "If you use the flag -profile you must set the profile output file to env variable PROFILE_OUTPUT_FILE"
fi

mkdir -p results
for test in _1_Int _2_String _3_IntString _4_IntStringJson _5_IntStringColumnNumber _6_String_NoAutocommit
do
	echo "Running test $test..."

	if ($profile)
	then
		java $PROFILE_OPTS -jar target/benchmarks.jar $test > results/$test.profile.output
		mv $PROFILE_OUTPUT_FILE results/$test.$PROFILE_OUTPUT_FILE
	fi

	java $JAVA_OPTS -jar target/benchmarks.jar $test > results/$test.output
done
