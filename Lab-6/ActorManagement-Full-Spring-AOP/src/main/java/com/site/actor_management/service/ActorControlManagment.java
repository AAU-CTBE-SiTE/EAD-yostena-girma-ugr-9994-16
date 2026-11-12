package com.site.actor_management.service;
import com.site.actor_management.model.Actor;
import com.site.actor_management.exception.ActorNotFoundException;
import com.site.actor_management.exception.ActorAlreadyExistsException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service
public class ActorControlManagment {
 private final List<Actor> actors=new ArrayList<>();
 public ActorControlManagment(){actors.add(new Actor(1,"John Doe","The Great Adventure"));actors.add(new Actor(2,"Jane Smith","Mystery Island"));}
 public List<Actor> getAllActors(){return actors;}
 public Actor getActorById(int id){return find(id);}
 public Actor addActor(Actor actor){
  if(actor==null) throw new IllegalArgumentException("Actor body is required.");
  if(actors.stream().anyMatch(a->a.getId()==actor.getId())) throw new ActorAlreadyExistsException("Actor ID already exists: "+actor.getId());
  actors.add(actor); return actor;
 }
 public Actor updateActor(int id,Actor updated){Actor a=find(id);a.setName(updated.getName());a.setMovieName(updated.getMovieName());return a;}
 public Actor patchActor(int id,Actor updated){Actor a=find(id);if(updated.getName()!=null)a.setName(updated.getName());if(updated.getMovieName()!=null)a.setMovieName(updated.getMovieName());return a;}
 public void deleteActor(int id){actors.remove(find(id));}
 private Actor find(int id){return actors.stream().filter(a->a.getId()==id).findFirst().orElseThrow(()->new ActorNotFoundException("Actor not found: "+id));}
}
