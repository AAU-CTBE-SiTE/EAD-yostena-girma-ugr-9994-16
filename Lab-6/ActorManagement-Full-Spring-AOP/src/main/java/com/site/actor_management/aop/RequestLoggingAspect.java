package com.site.actor_management.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Arrays;
@Aspect @Component @Order(1)
public class RequestLoggingAspect {
 @Before("com.site.actor_management.aop.ActorPointcuts.actorServiceMethods()")
 public void logBefore(JoinPoint jp){
  System.out.println("\n[AOP @Before] Method: "+jp.getSignature().toShortString());
  System.out.println("[AOP @Before] Arguments: "+Arrays.toString(jp.getArgs()));
 }
}
