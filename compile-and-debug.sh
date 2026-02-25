mvn compile -DskipTests && java -ea -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -cp "core/target/classes:core/target/lib/*" org.north.core.MainThread
