/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
public @interface Context {

    Class<? extends AnnotatedEntity>[] value() default AnnotatedEntity.class;
}
