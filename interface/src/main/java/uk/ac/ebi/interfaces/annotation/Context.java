package uk.ac.ebi.interfaces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import uk.ac.ebi.interfaces.AnnotatedEntity;


/**
 * 
 * Context provides a guide for restriction on where annotations can
 * be mapped
 * 
 * @author johnmay
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Context {

    Class<? extends AnnotatedEntity>[] value() default AnnotatedEntity.class;
}
