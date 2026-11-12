package com.site.actor_management.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Aspect @Component @Order(4)
public class PerformanceAspect {
 @Around("com.site.actor_management.aop.ActorPointcuts.actorServiceMethods()")
 public Object measure(ProceedingJoinPoint pjp) throws Throwable {
  long start=System.nanoTime();
  System.out.println("[AOP @Around] Before proceed: "+pjp.getSignature().toShortString());
  try {
   Object result=pjp.proceed();
   System.out.println("[AOP @Around] After proceed.");
   return result;
  } catch(Throwable t) {
   System.out.println("[AOP @Around] Exception passed through.");
   throw t;
  } finally {
   System.out.println("[AOP @Around] Execution time: "+((System.nanoTime()-start)/1_000_000.0)+" ms");
  }
 }
}
