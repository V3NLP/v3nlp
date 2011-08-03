 @echo off
 @set JVM_OPTS=-XX:MaxPermSize=256m -XX:PermSize=128m -Xms128m -Xmx512m
 (cd .. && java %JVM_OPTS% -jar lib/start-6.1.23.jar)
