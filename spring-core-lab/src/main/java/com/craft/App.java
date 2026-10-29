package com.craft;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("beansConfig.xml");

        Student student = context.getBean("student", Student.class);

        System.out.println(student);
        student.getDetails();

        ((ClassPathXmlApplicationContext) context).close();
    }
}
