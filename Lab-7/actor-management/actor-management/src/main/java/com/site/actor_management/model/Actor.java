package com.site.actor_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String movie;

    public Actor() {}

    public Actor(String name, String movie) {
        this.name = name;
        this.movie = movie;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMovie() { return movie; }
    public void setMovie(String movie) { this.movie = movie; }
}