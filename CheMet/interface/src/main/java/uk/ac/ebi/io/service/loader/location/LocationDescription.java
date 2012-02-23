package uk.ac.ebi.io.service.loader.location;

/**
 * LocationDescription - 23.02.2012 <br/>
 * <p/>
 * Class describes a required {@see ResourceLocation} for a
 * {@see ResourceLoader}. It provides methods for name and
 * description access as well as type storage and a default
 * location
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LocationDescription {

    /**
     * Short name of the required location (e.g. ChEBI SDF File)
     *
     * @return the name
     */
    public String getName();

    /**
     * A longer description, usually describing some bound the contents
     * of the file should follow (e.g. SDF file containing 'ChEBI ID' property fields)
     *
     * @return description
     */
    public String getDescription();

    /**
     * Access a bounding type on the location (e.g. ResourceFileLocation)
     * indicates we need a file an not a directory
     *
     * @return
     */
    public Class<? extends ResourceLocation> getType();

    /**
     * Determine whether the location has an associated default
     * @return whether the description has a default location
     */
    public boolean hasDefault();

    /**
     * Access the default location (if specified)
     * @return default location
     */
    public ResourceLocation getDefault();

}
