package com.site.actor_management.exception;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ActorNotFoundException.class)
 public ResponseEntity<Map<String,String>> notFound(ActorNotFoundException e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Not Found","message",e.getMessage()));}
 @ExceptionHandler(ActorAlreadyExistsException.class)
 public ResponseEntity<Map<String,String>> conflict(ActorAlreadyExistsException e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Conflict","message",e.getMessage()));}
 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<Map<String,String>> badRequest(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("error","Bad Request","message",e.getMessage()));}
}
