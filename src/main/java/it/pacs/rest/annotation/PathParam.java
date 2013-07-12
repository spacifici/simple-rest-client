/*
 * Copyright (C) 2013 Stefano Pacifici
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.pacs.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a parameter as a path parameter.<br>
 * <p/>
 * <pre>
 * &#064;Path(&quot;/events/{id}/time&quot;)
 * String getEventTime(@PathParam(&quot;id&quot;) int identifier);
 * </pre>
 * <p/>
 * &#123;id&#125; will be substitute by identifier to generate the correct url. <br>
 * <strong>The parameter type as to be a primitive type, a String or has to
 * override the toString() method</strong>
 *
 * @author Stefano Pacifici
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {
    String value() default "";
}
