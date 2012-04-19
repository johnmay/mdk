package uk.ac.ebi.mdk.domain.tool;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.reaction.Compartment;

import java.awt.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DialogCompartmentResolver implements CompartmentResolver {

    private static final Logger LOGGER = Logger.getLogger(DialogCompartmentResolver.class);

    private CompartmentResolver resolver;
    private Window              window;


    public DialogCompartmentResolver(CompartmentResolver parent,
                                     Window window){

    }

    @Override
    public Compartment getCompartment(String compartment) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
