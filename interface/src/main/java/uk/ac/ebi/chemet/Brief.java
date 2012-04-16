package uk.ac.ebi.chemet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides a brief description for the what the class is doing
 * and is available are runtime. The default value the 'brief' is
 * 'nknown'
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Brief {
    String value() default "Unknown";
}
