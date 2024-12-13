package com.ravali.spbt.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Movie {

     private Long id;
     private String name;
     private String director;
     private List<String> actors = new ArrayList<>();

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getDirector() {
          return director;
     }

     public void setDirector(String director) {
          this.director = director;
     }

     public List<String> getActors() {
          return actors;
     }

     public void setActors(List<String> actors) {
          this.actors = actors;
     }
}
