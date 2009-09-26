Usage:

1. Copy jar to M2_HOME/boot
2. Copy the M2_HOME/bin/mvn script to M2_HOME/bin/mvn2
3. Edit the M2_HOME/bin/mvn2 script and change the last line from:

exec "$JAVACMD" \
  $MAVEN_OPTS \
  -cp "${M2_HOME}"/boot/classworlds-*.jar \
  "-Dclassworlds.conf=${M2_HOME}/bin/m2.conf" \
  "-Dmaven.home=${M2_HOME}"  \
  ${CLASSWORLDS_LAUNCHER} $QUOTED_ARGS

to:

exec "$JAVACMD" \
  $MAVEN_OPTS \
  -cp "${M2_HOME}"/boot/classworlds-1.1.jar:"${M2_HOME}"/boot/maven-trap-0.5.jar \
  "-Dclassworlds.conf=${M2_HOME}/bin/m2.conf" \
  "-Dmaven.home=${M2_HOME}"  \
  org.twdata.maven.trap.Dispatcher $QUOTED_ARGS

3. Add the M2_HOME/bin/mvn2 to your path

4. Switch on whatever features you'd like by setting environment variables:
   - MAVEN_COLOR : Output colorization
   - MAVEN_ALWAYS_OFFLINE : Changes default to be offline, -o to go online
   - MAVEN_YAMLPOM : Automatic creating and syncing of YAML version of the POM

Now, you can use these features via 'mvn2' or execute as normal via 'mvn'
