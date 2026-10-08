# JDBC Lab 1 — StudentsDB

Java JDBC lab connecting to MySQL: create a database/table, insert/update/delete rows, query data, and compute an average grade.

Based on the JDBC Lab Manual (Yared Y., November 2023).

## Prerequisites

- JDK 21 (or compatible)
- MySQL Server running on `localhost:3306`
- MySQL Connector/J (included via Maven / `lib/`)

## Project layout

```
jdbc_lab1/
├── pom.xml
├── lib/mysql-connector-j-8.3.0.jar
└── src/main/java/com/itsc/
    ├── JDBCDemo.java                      # basic connection demo
    └── StudentDatabaseOperations.java     # Tasks 1–6
```

## Credentials

Default credentials match the lab manual (`root` / `root`). Override if yours differ:

| Variable         | Default | Meaning        |
|------------------|---------|----------------|
| `MYSQL_USER`     | `root`  | MySQL username |
| `MYSQL_PASSWORD` | `root`  | MySQL password |

Or edit the constants at the top of `StudentDatabaseOperations.java` / `JDBCDemo.java`.

## What the program does

`StudentDatabaseOperations` runs these steps in order:

1. **Connect & create** — creates `StudentsDB` and table `students` (`id`, `firstname`, `lastname`, `grade`)
2. **Insert** — one sample row + ten more rows
3. **Retrieve** — prints five rows (`SELECT … LIMIT 5`)
4. **Update** — changes `firstname` for student `id = 1`
5. **Delete** — removes student `id = 2`
6. **Average** — prints `AVG(grade)` for all remaining students

## Run in IntelliJ IDEA

1. Open this folder as a project (Maven or plain Java module).
2. Ensure MySQL is running and credentials are correct.
3. Run `com.itsc.StudentDatabaseOperations`.

If the driver is missing from the classpath, add `lib/mysql-connector-j-8.3.0.jar` via **File → Project Structure → Libraries**, or reload the Maven project so `pom.xml` pulls the dependency.

## Run from the terminal

PowerShell (set your real password first):

```powershell
$env:MYSQL_PASSWORD = "YOUR_PASSWORD"

# Compile
javac -cp "lib\mysql-connector-j-8.3.0.jar" -d out src\main\java\com\itsc\*.java

# Run full lab
java -cp "out;lib\mysql-connector-j-8.3.0.jar" com.itsc.StudentDatabaseOperations

# Optional: connection-only demo
java -cp "out;lib\mysql-connector-j-8.3.0.jar" com.itsc.JDBCDemo
```

With Maven (if installed):

```powershell
$env:MYSQL_PASSWORD = "YOUR_PASSWORD"
mvn -q compile exec:java -Dexec.mainClass="com.itsc.StudentDatabaseOperations"
```

## JDBC flow (reference)

1. Register / load driver — `Class.forName("com.mysql.cj.jdbc.Driver")` (optional with JDBC 4.0+)
2. Open connection — `DriverManager.getConnection(url, user, password)`
3. Create statement — `Statement` or `PreparedStatement`
4. Execute queries — `executeQuery` / `executeUpdate`
5. Close resources — try-with-resources in this project

Connection URL used:

```
jdbc:mysql://localhost:3306/StudentsDB
```
