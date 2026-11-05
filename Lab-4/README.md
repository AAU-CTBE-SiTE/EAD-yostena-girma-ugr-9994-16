# Spring Core IoC and Bean Management Lab

This project follows the final combined example in the supplied lab PDF:
- Spring IoC container loads `beansConfig.xml`
- Student bean receives literal values through setter injection
- Student bean receives an `Address` bean through object injection
- Student bean receives a list of `Subject` beans through collection injection
- The bean is configured with singleton scope for the scope demonstration

## Run in IntelliJ IDEA
1. Extract this folder.
2. Open the `spring-core-lab` folder in IntelliJ IDEA.
3. Reload Maven dependencies.
4. Set the project SDK and Maven importer JDK to Java 19.
5. Run `src/main/java/com/craft/App.java`.

Expected output includes the student details, Addis/Ethiopia address, and the two subjects.

## Bean scope check from the PDF
To demonstrate prototype scope, change `scope="singleton"` in `beansConfig.xml` to `scope="prototype"`.
The PDF's scope example uses two calls to `context.getBean("student", Student.class)` and prints whether the references are identical (`true` for singleton, `false` for prototype). The main application above focuses on the final collection-injection example.
