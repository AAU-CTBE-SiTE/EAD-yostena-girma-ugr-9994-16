package com.craft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static EntityManagerFactory emf = createEntityManagerFactory();

    private static EntityManagerFactory createEntityManagerFactory() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("jakarta.persistence.jdbc.url", required("DB_URL"));
        settings.put("jakarta.persistence.jdbc.user", required("DB_USER"));
        settings.put("jakarta.persistence.jdbc.password", required("DB_PASSWORD"));
        return Persistence.createEntityManagerFactory("studentPU", settings);
    }

    private static String required(String name) {
        String value = setting(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing " + name + ". Set it as an environment variable or in the .env file. See DATABASE.md.");
        }
        return value;
    }

    private static String setting(String name) {
        String fromEnvironment = System.getenv(name);
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return fromEnvironment;
        }
        return dotenv().get(name);
    }

    private static Map<String, String> dotenv() {
        Map<String, String> values = new HashMap<>();
        Path workingDirectory = Path.of(System.getProperty("user.dir"));
        Path envFile = workingDirectory.resolve(".env");
        if (!Files.exists(envFile)) {
            envFile = workingDirectory.resolve("untitled").resolve(".env");
        }
        if (!Files.exists(envFile)) {
            return values;
        }
        try {
            for (String line : Files.readAllLines(envFile)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                if (separator > 0) {
                    values.put(trimmed.substring(0, separator).trim(), trimmed.substring(separator + 1).trim());
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read " + envFile, exception);
        }
        return values;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void createStudent(Student student) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(student);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    public static void updateStudentName(String newName, int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            student.setName(newName);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    public static void updateStudentNameAndTech(String newName, String newTech, int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            student.setName(newName);
            student.setTech(newTech);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    public static Student findStudentById(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(Student.class, id);
        } finally {
            entityManager.close();
        }
    }

    public static boolean containsStudent(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            Student student = entityManager.find(Student.class, id);
            return student != null && entityManager.contains(student);
        } finally {
            entityManager.close();
        }
    }

    public static void removeStudentById(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, id);
            if (student != null && entityManager.contains(student)) {
                entityManager.remove(student);
            }
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    // 1. Select all students
    public static List<Student> getAllStudents() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 2. Select student by ID
    public static Student getStudentById(int id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id = :id", Student.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // 3. Select students by name (exact match)
    public static List<Student> getStudentsByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.name = :name", Student.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 4. Select students by name (case-insensitive)
    public static List<Student> getStudentsByNameCaseInsensitive(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE LOWER(s.name) = LOWER(:name)", Student.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 5. Select students by technology
    public static List<Student> getStudentsByTech(String tech) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech = :tech", Student.class);
            query.setParameter("tech", tech);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 6. Select students with names starting with a letter
    public static List<Student> getStudentsByNameStartingWith(String letter) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.name LIKE :pattern", Student.class);
            query.setParameter("pattern", letter + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 7. Select students with names containing a substring
    public static List<Student> getStudentsByNameContaining(String substring) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.name LIKE :pattern", Student.class);
            query.setParameter("pattern", "%" + substring + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 8. Select students with ID greater than a value
    public static List<Student> getStudentsWithIdGreaterThan(int minId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id > :minId", Student.class);
            query.setParameter("minId", minId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 9. Select students with ID in a range
    public static List<Student> getStudentsWithIdBetween(int startId, int endId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id BETWEEN :startId AND :endId", Student.class);
            query.setParameter("startId", startId);
            query.setParameter("endId", endId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 10. Select students with specific IDs (IN clause)
    public static List<Student> getStudentsByIdList(List<Integer> idList) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id IN :idList", Student.class);
            query.setParameter("idList", idList);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 11. Select all students ordered by name (ascending)
    public static List<Student> getAllStudentsOrderByNameAsc() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.name ASC", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 12. Select all students ordered by name (descending)
    public static List<Student> getAllStudentsOrderByNameDesc() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.name DESC", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 13. Select first N students (pagination)
    public static List<Student> getFirstNStudents(int n) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.id ASC", Student.class);
            query.setMaxResults(n);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 14. Select students with pagination (offset)
    public static List<Student> getStudentsWithPagination(int offset, int limit) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.id ASC", Student.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 15. Count all students
    public static long countAllStudents() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(s) FROM Student s");
            return (long) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // 16. Count students by technology
    public static List<Object[]> countStudentsByTech() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT s.tech, COUNT(s) FROM Student s GROUP BY s.tech");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 17. Find distinct technologies
    public static List<String> getDistinctTechs() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT DISTINCT s.tech FROM Student s", String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 18. Find average ID
    public static double getAverageId() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT AVG(s.id) FROM Student s");
            return (double) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // 19. Find maximum ID
    public static int getMaxId() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT MAX(s.id) FROM Student s");
            return (int) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // 20. Find students with the longest names
    public static List<Student> getStudentsWithLongestNames() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE LENGTH(s.name) = " +
                            "(SELECT MAX(LENGTH(s2.name)) FROM Student s2)", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 21. Select students with a specific technology and name pattern
    public static List<Student> getStudentsByTechAndNamePattern(String tech, String pattern) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech = :tech AND s.name LIKE :pattern",
                    Student.class);
            query.setParameter("tech", tech);
            query.setParameter("pattern", "%" + pattern + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 22. Select students with NULL technology
    public static List<Student> getStudentsWithNullTech() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech IS NULL", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 23. Select students with non-NULL technology
    public static List<Student> getStudentsWithNonNullTech() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech IS NOT NULL", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 24. Update student's technology
    public static int updateStudentTech(int id, String newTech) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE Student s SET s.tech = :newTech WHERE s.id = :id");
            query.setParameter("newTech", newTech);
            query.setParameter("id", id);
            int updatedCount = query.executeUpdate();
            tx.commit();
            return updatedCount;
        } finally {
            em.close();
        }
    }

    // 25. Update all students' technology by pattern
    public static int updateTechByPattern(String oldPattern, String newTech) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery(
                    "UPDATE Student s SET s.tech = :newTech WHERE s.tech LIKE :oldPattern");
            query.setParameter("newTech", newTech);
            query.setParameter("oldPattern", "%" + oldPattern + "%");
            int updatedCount = query.executeUpdate();
            tx.commit();
            return updatedCount;
        } finally {
            em.close();
        }
    }

    // 26. Delete student by ID
    public static int deleteStudentById(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("DELETE FROM Student s WHERE s.id = :id");
            query.setParameter("id", id);
            int deletedCount = query.executeUpdate();
            tx.commit();
            return deletedCount;
        } finally {
            em.close();
        }
    }

    // 27. Delete students by technology
    public static int deleteStudentsByTech(String tech) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("DELETE FROM Student s WHERE s.tech = :tech");
            query.setParameter("tech", tech);
            int deletedCount = query.executeUpdate();
            tx.commit();
            return deletedCount;
        } finally {
            em.close();
        }
    }

    // 28. Select students with the most common technology
    public static List<Student> getStudentsWithMostCommonTech() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech = (" +
                            "SELECT s2.tech FROM Student s2 GROUP BY s2.tech " +
                            "ORDER BY COUNT(s2) DESC)",
                    Student.class);
            query.setMaxResults(1);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 29. Select students with unique technologies
    public static List<Student> getStudentsWithUniqueTechs() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech IN (" +
                            "SELECT s2.tech FROM Student s2 GROUP BY s2.tech HAVING COUNT(s2) = 1)",
                    Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 30. Select students with name length greater than average
    public static List<Student> getStudentsWithLongerThanAverageNames() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE LENGTH(s.name) > " +
                            "(SELECT AVG(LENGTH(s2.name)) FROM Student s2)",
                    Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<String> getAllStudentNames() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery("SELECT s.name FROM Student s", String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Student> getStudentsByNameAndTech(String name, String tech) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.name = :name AND s.tech = :tech", Student.class);
            query.setParameter("name", name);
            query.setParameter("tech", tech);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Student> getStudentsByTechIn(List<String> techs) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech IN :techs", Student.class);
            query.setParameter("techs", techs);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Student> getAllStudentsOrderByTechDesc() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s ORDER BY s.tech DESC", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static long countStudentsWithTech(String tech) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(s) FROM Student s WHERE s.tech = :tech");
            query.setParameter("tech", tech);
            return (long) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public static int getMinId() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT MIN(s.id) FROM Student s");
            return ((Number) query.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public static long getSumOfIds() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT SUM(s.id) FROM Student s");
            return ((Number) query.getSingleResult()).longValue();
        } finally {
            em.close();
        }
    }

    public static List<Student> getStudentsByTechPositional(String tech) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech = ?1", Student.class);
            query.setParameter(1, tech);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static int updateStudentsTech(String oldTech, String newTech) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery(
                    "UPDATE Student s SET s.tech = :newTech WHERE s.tech = :oldTech");
            query.setParameter("newTech", newTech);
            query.setParameter("oldTech", oldTech);
            int updatedCount = query.executeUpdate();
            tx.commit();
            return updatedCount;
        } finally {
            em.close();
        }
    }

    public static int updateStudentNameByQuery(String newName, int id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE Student s SET s.name = :newName WHERE s.id = :id");
            query.setParameter("newName", newName);
            query.setParameter("id", id);
            int updatedCount = query.executeUpdate();
            tx.commit();
            return updatedCount;
        } finally {
            em.close();
        }
    }

    public static int deleteStudentsByName(String name) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("DELETE FROM Student s WHERE s.name = :name");
            query.setParameter("name", name);
            int deletedCount = query.executeUpdate();
            tx.commit();
            return deletedCount;
        } finally {
            em.close();
        }
    }

    public static List<Object[]> getTechsWithMoreThan(long minCount) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery(
                    "SELECT s.tech, COUNT(s) FROM Student s GROUP BY s.tech HAVING COUNT(s) > :minCount");
            query.setParameter("minCount", minCount);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static Student getStudentWithHighestId() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.id = (SELECT MAX(s2.id) FROM Student s2)",
                    Student.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public static List<Student> getStudentsWithSameTechAs(int id) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.tech = " +
                            "(SELECT s2.tech FROM Student s2 WHERE s2.id = :id)",
                    Student.class);
            query.setParameter("id", id);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Student> getStudentsWhoShareTech() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE EXISTS " +
                            "(SELECT s2 FROM Student s2 WHERE s2.tech = s.tech AND s2.id != s.id)",
                    Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<String> getUppercaseNames() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT UPPER(s.name) FROM Student s", String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Integer> getNameLengths() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT LENGTH(s.name) FROM Student s", Integer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("John Snow");
        student.setTech("Java");
        createStudent(student);

        List<Student> allStudents = getAllStudents();
        System.out.println("All students: " + allStudents.size());

        List<Student> javaStudents = getStudentsByTech("Java");
        System.out.println("Java students: " + javaStudents.size());

        int updated = updateStudentTech(1, "Python");
        System.out.println("Updated " + updated + " student(s)");

        emf.close();
    }
}
