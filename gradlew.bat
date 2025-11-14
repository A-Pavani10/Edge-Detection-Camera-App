@echo off
set DIR=%~dp0
set APP_HOME=%DIR%
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

"%JAVA_HOME%\bin\java.exe" %DEFAULT_JVM_OPTS% ^
  -Dfile.encoding=UTF-8 ^
  -classpath %CLASSPATH% ^
  org.gradle.wrapper.GradleWrapperMain %*
