#!/bin/sh
set -e

echo "$(date '+%Y-%m-%d %H:%M:%S') mvn clean"
mvn clean

echo "0/10 $(date '+%Y-%m-%d %H:%M:%S') start run all embedded database test"

echo "1/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-hibernate,embedded-h2"
mvn test -P jpa-hibernate,embedded-h2

echo "2/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-eclipselink,embedded-h2"
mvn test -P jpa-eclipselink,embedded-h2

echo "3/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-hibernate,embedded-hsql"
mvn test -P jpa-hibernate,embedded-hsql

echo "4/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-eclipselink,embedded-hsql"
mvn test -P jpa-eclipselink,embedded-hsql

echo "5/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-hibernate,embedded-derby"
mvn test -P jpa-hibernate,embedded-derby

echo "6/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-eclipselink,embedded-derby"
mvn test -P jpa-eclipselink,embedded-derby

echo "7/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-hibernate,embedded-postgres"
mvn test -P jpa-hibernate,embedded-postgres

echo "8/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-eclipselink,embedded-postgres"
mvn test -P jpa-eclipselink,embedded-postgres

echo "9/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-hibernate,embedded-mysql"
mvn test -P jpa-hibernate,embedded-mysql

echo "10/10 $(date '+%Y-%m-%d %H:%M:%S') mvn test -P jpa-eclipselink,embedded-mysql"
mvn test -P jpa-eclipselink,embedded-mysql

echo "10/10 $(date '+%Y-%m-%d %H:%M:%S') end run all embedded database test"

echo "if want to run test on host database, manual run bellow command:"
echo "mvn test -P jpa-hibernate,postgres \\"
echo "&& mvn test -P jpa-eclipselink,postgres \\"
echo "&& mvn test -P jpa-hibernate,mysql \\"
echo "&& mvn test -P jpa-eclipselink,mysql"