package uk.ac.ebi.io.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;

import java.util.regex.Pattern;

/**
 * ResourceLocationDescription.java - 22.02.2012 <br/>
 * <p/>
 * Describes a {@see ResourceLocation} providing a name, description and default
 * value. Note the class of the required ResourceLocation is passed to ensure new
 * entries conform to required type (i.e. you can't provide a directory to a loader
 * that is expected a file).
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LocationDescription {

    private static final Logger LOGGER = Logger.getLogger(LocationDescription.class);
    private String name;
    private String description;
    private String key;
    private Class c;
    private ResourceLocation defaultLocation;
    
    // Pattern to match one or more spaces, this is used to replace those
    // spaces with '.'
    private static final Pattern KEY_MAKER = Pattern.compile("\\s+");

    /**
     * Constructor creates a description of a location that has a
     * specified default location
     *
     * @param name
     * @param description
     * @param c
     * @param defaultLocation
     * @param <T>
     */
    public <T extends ResourceLocation> LocationDescription(String name,
                                                            String description,
                                                            Class<T> c,
                                                            T defaultLocation) {

        this.name = name;
        this.key = createKey(name);
        this.description = description;
        this.c = c;
        this.defaultLocation = defaultLocation;
    }

    /**
     * Constructs a description with no default location
     *
     * @param name
     * @param description
     * @param c
     * @param <T>
     */
    public <T extends ResourceLocation> LocationDescription(String name,
                                                            String description,
                                                            Class<T> c) {
        this.name = name;
        this.key = createKey(name);
        this.description = description;
        this.c = c;
    }
    
    public static String createKey(String name){
        return KEY_MAKER.matcher(name.toLowerCase()).replaceAll(".");
    }
    
    public String getKey(){
        return key;
    }
    
    public String getName(){
        return name;    
    }
    
    public String getDescription(){
        return description;
    }
    
    public boolean hasDefaultLocation(){
        return defaultLocation != null;
    }

    public ResourceLocation getDefaultLocation(){
        return defaultLocation;
    }



}
