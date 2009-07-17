Usage:

1. Copy jar to M2_HOME/lib
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
  -cp "${M2_HOME}"/boot/classworlds-1.1.jar:"${M2_HOME}"/lib/console-colorizer-1.0-SNAPSHOT.jar \
  "-Dclassworlds.conf=${M2_HOME}/bin/m2.conf" \
  "-Dmaven.home=${M2_HOME}"  \
  org.twdata.maven.colorizer.App $QUOTED_ARGS

3. Add the M2_HOME/bin/mvn2 to your path

Now, you can use colorised output via 'mvn2' or execute as normal via 'mvn'
