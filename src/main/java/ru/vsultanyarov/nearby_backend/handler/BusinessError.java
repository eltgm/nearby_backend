package ru.vsultanyarov.nearby_backend.handler;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BusinessError {
    HttpStatus status() default HttpStatus.BAD_REQUEST;

    /**
     * Business error's code
     */
    String errorCode() default "";

    /**
     * Business error's message
     */
    String message() default "";

    /**
     * Empty errorCode
     */
    boolean isEmptyErrorCode() default false;
}
