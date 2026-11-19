package com.site.actor_management.controller;

import com.site.actor_management.model.Actor;
import com.site.actor_management.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public Optional<Actor> getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.saveActor(actor);
    }

    @DeleteMapping("/{id}")
    public String deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return "Actor with ID " + id + " deleted successfully.";
    }
}
