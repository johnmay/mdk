package uk.ac.ebi.annotation.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.Descriptor;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;

/**
 * GibbsEnergy - 16.03.2012 <br/>
 * <p/>
 * Stores the Gibbs energy of a reaction
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Context(MetabolicReaction.class)
@Descriptor(brief       = "Gibbs energy (ΔG)",
            description = "Thermodynamic potential of the reaction")
public class GibbsEnergy
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergy.class);

    public GibbsEnergy() {
        super();
    }

    public GibbsEnergy(Double value) {
        super(value);
    }

    @Override
    public GibbsEnergy newInstance() {
        return new GibbsEnergy();
    }
}


