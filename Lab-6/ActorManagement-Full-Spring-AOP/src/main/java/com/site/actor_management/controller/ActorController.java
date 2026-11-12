package com.site.actor_management.controller;
import com.site.actor_management.model.Actor;
import com.site.actor_management.service.ActorControlManagment;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1")
public class ActorController {
 private final ActorControlManagment service;
 public ActorController(ActorControlManagment service){this.service=service;}
 @GetMapping("/actor") public List<Actor> getActorDetails(){return service.getAllActors();}
 @GetMapping("/actor/{id}") public Actor getActorById(@PathVariable int id){return service.getActorById(id);}
 @PostMapping("/actor") public Actor addActor(@RequestBody Actor actor){return service.addActor(actor);}
 @PutMapping("/actor/{id}") public Actor updateActor(@PathVariable int id,@RequestBody Actor actor){return service.updateActor(id,actor);}
 @PatchMapping("/actor/{id}") public Actor patchActor(@PathVariable int id,@RequestBody Actor actor){return service.patchActor(id,actor);}
 @DeleteMapping("/actor/{id}") public String deleteActor(@PathVariable int id){service.deleteActor(id);return "Actor deleted successfully!";}
}
