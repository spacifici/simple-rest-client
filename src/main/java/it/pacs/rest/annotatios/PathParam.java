package it.pacs.rest.annotatios;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a parameter as a path parameter.<br>
 * 
 * <pre>
 * &#064;Path(&quot;/events/{id}/time&quot;)
 * String getEventTime(@PathParam(&quot;id&quot;) int identifier);
 * </pre>
 * 
 * &#123;id&#125; will be substitute by identifier to generate the correct url. <br>
 * <strong>The parameter type as to be a primitive type, a String or has to
 * override the toString() method</strong>
 * 
 * @author Stefano Pacifici
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {
	String value() default "";
}
