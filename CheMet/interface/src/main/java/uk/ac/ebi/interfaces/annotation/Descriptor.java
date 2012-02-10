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
 * Descriptor provides a brief and long description of an annotation, 
 * observation or identifier
 * 
 * @author johnmay
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Descriptor {

    String brief();
    String description();
    
    
}
