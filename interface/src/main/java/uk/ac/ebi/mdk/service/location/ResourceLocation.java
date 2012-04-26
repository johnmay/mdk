package uk.ac.ebi.mdk.service.location;

/**
 * ResourceLocation - 21.02.2012 <br/>
 * <p/>
 * Describes a location of a resource that can be loaded. This interface
 * serves a base for other resource locations to build upon and it's primary
 * function is determine whether the resource is available to be loaded.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceLocation {

    /**
     * Method determines if the location defined by the resource
     * is available for the loader to use. On a local system file
     * this could be whether the file exists, whilst on a remote
     * location (e.g. FTP) it could check the connection.
     *
     * @return whether the resource location is available
     */
    public boolean isAvailable();

}
