package uk.ac.ebi.mdk.lang.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides a description for the what the class is doing
 * and is available are runtime. The default value the 'description' is
 * 'Unavailable'
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value() default "No description available";
}
