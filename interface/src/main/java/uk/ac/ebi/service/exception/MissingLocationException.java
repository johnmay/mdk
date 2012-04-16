package uk.ac.ebi.service.exception;

/**
 * MissingLocationException.java - 20.02.2012 <br/>
 * <p/>
 * Exception should thrown when a loader is missing a required {@see ResourceLocation}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class MissingLocationException extends RuntimeException {

    /**
     * Construct an instance of the exception with the specified message
     *
     * @param message details of what resource location is missing
     */
    public MissingLocationException(String message) {
        super(message);
    }


}
