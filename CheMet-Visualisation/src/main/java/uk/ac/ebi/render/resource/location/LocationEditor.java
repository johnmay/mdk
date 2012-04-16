package uk.ac.ebi.render.resource.location;

import uk.ac.ebi.service.location.LocationDescription;
import uk.ac.ebi.service.location.ResourceLocation;

import java.io.IOException;

/**
 * LocationEditor - 27.02.2012 <br/>
 * <p/>
 * A location editor defines an editor to create/edit ResourceLocation's
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LocationEditor {

    public ResourceLocation getResourceLocation() throws IOException;

    public void setup(LocationDescription description);

}
