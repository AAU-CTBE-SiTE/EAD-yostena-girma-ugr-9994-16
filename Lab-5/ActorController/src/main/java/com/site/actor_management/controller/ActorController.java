package com.site.actor_management.controller;

import com.site.actor_management.model.Actor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ActorController {

    List<Actor> actors = new ArrayList<>();

    public ActorController() {
        actors.add(new Actor(1, "John Doe", "The Great Adventure"));
        actors.add(new Actor(2, "Jane Smith", "Mystery Island"));
    }

    // CRUD - READ (Get all actors)
    @GetMapping("/actor")
    public List<Actor> getActorDetails() {
        return actors;
    }

    // CRUD - READ (Get actor by ID)
    @GetMapping("/actor/{id}")
    public Actor getActorById(@PathVariable int id) {

        for (Actor actor : actors) {
            if (actor.getId() == id) {
                return actor;
            }
        }

        return null;
    }

    // CRUD - CREATE (Add a new actor)
    @PostMapping("/actor")
    public Actor addActor(@RequestBody Actor actor) {

        actors.add(actor);

        return actor;
    }

    // CRUD - UPDATE (Update an actor)
    @PutMapping("/actor/{id}")
    public Actor updateActor(
            @PathVariable int id,
            @RequestBody Actor updatedActor) {

        for (Actor actor : actors) {

            if (actor.getId() == id) {

                actor.setName(updatedActor.getName());
                actor.setMovieName(updatedActor.getMovieName());

                return actor;
            }
        }

        return null;
    }


    // CRUD - PARTIAL UPDATE
    @PatchMapping("/actor/{id}")
    public Actor patchActor(
            @PathVariable int id,
            @RequestBody Actor updatedActor) {

        for (Actor actor : actors) {

            if (actor.getId() == id) {

                if (updatedActor.getName() != null) {
                    actor.setName(updatedActor.getName());
                }

                if (updatedActor.getMovieName() != null) {
                    actor.setMovieName(updatedActor.getMovieName());
                }

                return actor;
            }
        }

        return null;
    }

    // CRUD - DELETE (Delete an actor)
    @DeleteMapping("/actor/{id}")
    public String deleteActor(@PathVariable int id) {

        for (Actor actor : actors) {

            if (actor.getId() == id) {

                actors.remove(actor);

                return "Actor deleted successfully!";
            }
        }

        return "Actor not found!";
    }
}