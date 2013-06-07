/**
 *
 */
package it.pacs.rest.annotatios;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as a REST service interface setting his base url.<br/>
 * This annotation is optional.
 *
 * @author Stefano Pacifici
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestService {
    /**
     * The base url of the REST service, by default it is {@code http://localhost:8080}
     *
     * @return the base url as a string
     */
    String value() default "http://localhost:8080";
}
