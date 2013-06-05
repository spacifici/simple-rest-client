package it.pacs.rest.annotatios;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface HeaderParam {
	String value() default "";
}
