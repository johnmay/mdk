package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.Descriptor;
import uk.ac.ebi.interfaces.annotation.Unique;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *          ChargeAnnotation 2012.02.14 <br/>
 *          Describes the charge on a metabolite
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
@Unique
@Context(Metabolite.class)
@Descriptor(brief       = "Charge",
            description = "The chare of this chemical")
public class Charge
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Charge.class);


    public Charge() {
    }


    public Charge(Double value) {
        super(value);
    }


    public Annotation newInstance() {
        return new Charge();
    }
}
