package by.alex.newsappmicriservice.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Данная аннотация выполняет роль маркера Create метода при использовании
 *  Spring AOP.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomCachebleCreate {
}
