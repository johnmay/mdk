package uk.ac.ebi.annotation.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.MetaInfo;
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
@MetaInfo(brief         = "Gibbs energy (ΔG)",
          description   = "Thermodynamic potential of the reaction")
public class GibbsEnergy
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergy.class);

    private Double error = 0d;
    
    public GibbsEnergy() {
        super();
    }

    public GibbsEnergy(Double value, Double error) {
        super(value);
        this.error = error;
    }

    @Override
    public GibbsEnergy newInstance() {
        return new GibbsEnergy();
    }

    /**
     * Access the error value
     * @return
     */
    public Double getError(){
        return this.error;
    }

    /**
     * Set the error for this annotation
     * @param error
     */
    public void setError(Double error) {
        this.error = error;
    }

    /**
     * Displays the Gibbs Energy value and it's error value in brackets
     * @inheritDoc
     */
    @Override
    public String toString(){
        return getValue() + " \u00B1 " + getError();
    }
    
}


