# Actor Management — Spring AOP concepts

A Spring Boot 3.4.5 / Java 17 CRUD project demonstrating the five core advice types:
`@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`, and `@Around`.

## Run
```bash
mvn spring-boot:run
```
Open `http://localhost:8080`.

## Endpoints
- GET `/api/v1/actor` — all actors
- GET `/api/v1/actor/1` — actor by ID
- POST `/api/v1/actor` — add actor; JSON: `{"id":3,"name":"Keanu Reeves","movieName":"The Matrix"}`
- PUT `/api/v1/actor/1` — update name/movie; JSON: `{"id":1,"name":"John Doe","movieName":"Mission Impossible"}`
- PATCH `/api/v1/actor/1` — partial update; JSON: `{"movieName":"John Wick"}`
- DELETE `/api/v1/actor/1` — delete actor

## AOP concepts in this project
1. Aspect: each class ending in `Aspect` groups cross-cutting behavior.
2. Join point: execution of a service method.
3. Pointcut: selects which join points are intercepted.
4. Advice: action performed at a selected join point.
5. `@Before`: logs before a service method.
6. `@After`: runs after completion, whether success or exception.
7. `@AfterReturning`: runs only after normal successful return; receives the result.
8. `@AfterThrowing`: runs only when the method throws; receives the exception.
9. `@Around`: wraps execution, can call `proceed()`, inspect result/errors and measure time.
10. `JoinPoint`: exposes method signature and arguments.
11. `ProceedingJoinPoint`: special join point used by around advice; `proceed()` continues the intercepted call.
12. Named reusable pointcut: `ActorPointcuts.actorServiceMethods()`.
13. `execution(...)` pointcut designator: selects public methods in `ActorControlManagment`.
14. `@Order`: determines aspect precedence (lower number has higher precedence on entry; exit order is reversed for nested advice).
15. Spring AOP proxy: Spring wraps the service bean; controller calls pass through the proxy.
16. Proxy limitation: self-invocation within the same bean bypasses proxy-based advice.
17. Exception propagation: `@AfterThrowing` observes an exception; it does not swallow it.

CRUD data is stored in an in-memory `ArrayList`; restarting the app resets the data. This is a teaching project, not production persistence.
