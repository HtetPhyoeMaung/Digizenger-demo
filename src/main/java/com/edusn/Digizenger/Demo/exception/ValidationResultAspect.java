package com.edusn.Digizenger.Demo.exception;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

@Aspect
@Configuration
public class ValidationResultAspect {

    @Pointcut("within(com.edusn.Digizenger.Demo.*.*.*)")
    public void apiClasses() {}

    @Before(value = "apiClasses() and args(..,result)", argNames = "result")
    public void handle(BindingResult result) {
        if(result.hasErrors()) {
            throw new ApiValidationException(result);
        }
    }
}