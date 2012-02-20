package uk.ac.ebi.io.service.exception;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * Exception should thrown when a loader is missing a required location
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class MissingLocationException extends Exception {
    
    public MissingLocationException(String message){
        super(message);
    }


}
