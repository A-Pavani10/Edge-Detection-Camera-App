#!/usr/bin/env sh
APP_HOME=$(cd "`dirname "$0"`" >/dev/null; pwd)
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
JAVA_CMD="java"
exec "$JAVA_CMD" $DEFAULT_JVM_OPTS \
  -Dfile.encoding=UTF-8 \
  -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain "$@"
