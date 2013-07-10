package it.pacs.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a parameter do be put in a request body.Useful for
 * POST and PUT methods.
 *
 * @author: Stefano Pacifici
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedParam {
    String value() default("");
}
