package com.craft;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.craft")
public class AppConfig {

    @Bean
    public Address address() {
        Address address = new Address();
        address.setCity("Addis");
        address.setCountry("Ethiopia");
        return address;
    }

    @Bean
    public Subject math() {
        Subject subject = new Subject();
        subject.setSubjectName("Maths 101");
        return subject;
    }

    @Bean
    public Subject javaEe() {
        Subject subject = new Subject();
        subject.setSubjectName("Java EE");
        return subject;
    }
}
