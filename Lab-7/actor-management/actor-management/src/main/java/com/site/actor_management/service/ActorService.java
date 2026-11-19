package com.site.actor_management.service;

import com.site.actor_management.model.Actor;
import com.site.actor_management.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Optional<Actor> getActorById(Long id) {
        return actorRepository.findById(id);
    }

    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }
}
