package com.site.actor_management.model;
public class Actor {
 private int id;
 private String name;
 private String movieName;
 public Actor() {}
 public Actor(int id, String name, String movieName) { this.id=id; this.name=name; this.movieName=movieName; }
 public int getId(){return id;} public void setId(int id){this.id=id;}
 public String getName(){return name;} public void setName(String name){this.name=name;}
 public String getMovieName(){return movieName;} public void setMovieName(String movieName){this.movieName=movieName;}
 @Override public String toString(){return "Actor{id="+id+", name='"+name+"', movieName='"+movieName+"'}";}
}
