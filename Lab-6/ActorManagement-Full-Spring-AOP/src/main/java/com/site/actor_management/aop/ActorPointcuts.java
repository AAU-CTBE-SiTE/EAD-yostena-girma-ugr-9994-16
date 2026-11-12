package com.site.actor_management.aop;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class ActorPointcuts {
 @Pointcut("execution(public * com.site.actor_management.service.ActorControlManagment.*(..))")
 public void actorServiceMethods() {}
}
