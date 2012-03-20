package uk.ac.ebi.annotation.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.Descriptor;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;

/**
 * GibbsEnergy - 16.03.2012 <br/>
 * <p/>
 * Stores the Gibbs energy error of a reaction
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Context(MetabolicReaction.class)
@Descriptor(brief       = "Gibbs energy (ΔG) error",
            description = "Thermodynamic potential of the reaction (usually when predicted)")
public class GibbsEnergyError
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergyError.class);

    public GibbsEnergyError() {
        super();
    }

    public GibbsEnergyError(Double value) {
        super(value);
    }

    @Override
    public GibbsEnergyError newInstance() {
        return new GibbsEnergyError();
    }
}


