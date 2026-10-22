# Database connection

The URL, user, and password are not stored in `persistence.xml`. The app reads them from environment variables. A local `.env` file is used when those variables are not set.

| Variable | Meaning | Example |
| --- | --- | --- |
| `DB_URL` | JDBC URL | `jdbc:mysql://localhost:3306/jpadb1` |
| `DB_USER` | MySQL user | `root` |
| `DB_PASSWORD` | MySQL password | your password |

## Option 1: `.env` file

1. In the `untitled` folder, copy `.env.example` to `.env`.
2. Put your real password in `DB_PASSWORD`.
3. Run `com.craft.App` from IntelliJ. The working directory must be the `untitled` folder.

`.env` stays on this machine. It is listed in `.gitignore`.

## Option 2: IntelliJ environment variables

1. Open Run, then Edit Configurations.
2. Select the `App` configuration.
3. In Environment variables, add `DB_URL`, `DB_USER`, and `DB_PASSWORD`.
4. Run `App` again.

Values set here override the `.env` file.

## Option 3: PowerShell

Run these in the same terminal before `java`, then start the app from that terminal:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/jpadb1"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your-password"
```

Create the database once in MySQL if it does not exist:

```sql
CREATE DATABASE jpadb1;
```
