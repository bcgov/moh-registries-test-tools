@ECHO OFF

PUSHD "%~dp0\..\"
SET BASE_DIR=%CD%
POPD

SET MAIN_JAR_FILENAME=${pom.artifactId}-${pom.version}.jar
SET MAIN_JAR=%BASE_DIR%\lib\%MAIN_JAR_FILENAME%

SET JVM=java
IF "%JAVA_HOME%" == "" GOTO DONE_JVM_SETUP
SET JVM=%JAVA_HOME%\bin\java
:DONE_JVM_SETUP

SET JAVA_OPTS=

"%JVM%" %JAVA_OPTS% -jar "%MAIN_JAR%" %*
